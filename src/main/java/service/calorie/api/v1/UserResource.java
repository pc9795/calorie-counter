package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import service.calorie.beans.ApiUserPrincipal;
import service.calorie.entities.User;
import service.calorie.exceptions.InvalidDataException;
import service.calorie.exceptions.ResourceNotExistException;
import service.calorie.repositories.UserRepository;
import service.calorie.util.Constants;

import javax.validation.Valid;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:15
 * Purpose: TODO:
 **/
@RestController()
@RequestMapping(Constants.ApiV1Resource.USER)
public class UserResource {

    private final UserRepository userRepository;

    @Autowired
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers(Pageable pageable) {
        // todo user manager is indirectly accessing meals.
        return userRepository.findAll(pageable).getContent();
    }

    @GetMapping("/{user_id}")
    public User getUser(@PathVariable("user_id") long userId) throws ResourceNotExistException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotExistException(String.format("No user with id: %s", userId));
        }
        // todo user manager is indirectly accessing meals.
        return user;
    }

    @PostMapping
    public User addUser(@Valid User user, ApiUserPrincipal principal) throws InvalidDataException {
        //Non admin can't add admin account.
        if (!principal.getUser().isAdmin() && user.isAdmin()) {
            throw new InvalidDataException("Non admin user can't add an admin user");
        }
        //todo what happens when calories are zero. Does api fetch logic work here.
        //todo expected calories logic.
        return userRepository.save(user);
    }

    @PutMapping("/{user_id}")
    public User updateUser(@Valid User user, @PathVariable("user_id") long userId, ApiUserPrincipal principal)
            throws InvalidDataException {
        //Non admin can't update to admin role.
        if (!principal.getUser().isAdmin() && user.isAdmin()) {
            throw new InvalidDataException("Non admin user can't update to an admin user");
        }

        User dbUser = userRepository.findById(userId);
        if (dbUser == null) {
            return userRepository.save(user);
        }
        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(user.getPassword());
        //todo check orphans are deleted or not.
        dbUser.setRoles(user.getRoles());
        //todo user manager has access to meals.
        //todo check orphans are deleted or not.
        //todo what happens when calories are zero. Does api fetch logic work here.
        //todo expected calories logic.
        dbUser.setMeals(user.getMeals());
        return userRepository.save(dbUser);
    }

    @DeleteMapping("/{user_id}")
    public void deleteUser(@PathVariable("user_id") long userId) throws ResourceNotExistException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotExistException(String.format("No user with id: %s", userId));
        }
        userRepository.delete(user);
    }

}
