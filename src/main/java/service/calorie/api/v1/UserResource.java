package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import service.calorie.config.ApiUserPrincipal;
import service.calorie.entities.User;
import service.calorie.exceptions.InvalidDataException;
import service.calorie.exceptions.ResourceNotExistException;
import service.calorie.exceptions.UserAlreadyExistException;
import service.calorie.repositories.UserRepository;
import service.calorie.utils.Constants;
import service.calorie.utils.SpecificationUtils;
import service.calorie.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:15
 * Purpose: REST user resource.
 **/
@RestController
@RequestMapping(Constants.ApiV1Resource.USER)
public class UserResource {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserResource(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Read all the users.
     *
     * @param pageable
     * @param search
     * @return
     * @throws InvalidDataException
     */
    @GetMapping
    public List<User> getUsers(Pageable pageable, @RequestParam(value = "search", required = false) String search)
            throws InvalidDataException {
        Specification<User> spec = search == null ? null :
                SpecificationUtils.getSpecFromQuery(search, SpecificationUtils::userAttributeConverter);
        return userRepository.findAll(spec, pageable).getContent();
    }


    /**
     * Read a single user.
     *
     * @param userId
     * @return
     * @throws ResourceNotExistException
     */
    @GetMapping("/{user_id}")
    public User getUser(@PathVariable("user_id") long userId) throws ResourceNotExistException {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotExistException(String.format("No user with id: %s", userId));
        }
        return user;
    }

    /**
     * Add a user. Non admin user can't create a user.
     *
     * @param user
     * @param principal
     * @return
     * @throws InvalidDataException
     */
    @PostMapping
    public User addUser(@Valid @RequestBody User user, Principal principal) throws InvalidDataException,
            UserAlreadyExistException {
        //Non admin can't add admin account.
        if (!Utils.isPrincipalAdmin(principal) && user.isAdmin()) {
            throw new InvalidDataException("Non admin user can't add an admin user");
        }
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException(String.format("User wih username: %s already exists",
                    user.getUsername()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update a user.
     *
     * @param user
     * @param userId
     * @param principal
     * @return
     * @throws InvalidDataException
     */
    @PutMapping("/{user_id}")
    public User updateUser(@RequestBody @Valid User user, @PathVariable("user_id") long userId,
                           Principal principal) throws InvalidDataException, UserAlreadyExistException {
        //Non admin can't update to admin role.
        if (!Utils.isPrincipalAdmin(principal) && user.isAdmin()) {
            throw new InvalidDataException("Non admin user can't update to an admin user");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //If not exist then save.
        User dbUser = userRepository.findById(userId);
        if (dbUser == null) {
            return userRepository.save(user);
        }
        String dbUsername = dbUser.getUsername();
        if (!dbUser.getUsername().equals(user.getUsername()) && userRepository.
                findUserByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException(String.format("User wih username: %s already exists",
                    user.getUsername()));
        }

        dbUser.setUsername(user.getUsername());
        dbUser.setPassword(user.getPassword());
        dbUser.setRoles(user.getRoles());
        dbUser.setUserSettings(user.getUserSettings());
        userRepository.save(dbUser);

        // We are changed details of the logged in user re-authenticate.
        if (dbUsername.equals(principal.getName())) {
            ApiUserPrincipal newPrincipal = new ApiUserPrincipal(dbUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(newPrincipal,
                    newPrincipal.getPassword(), newPrincipal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return dbUser;
    }

    /**
     * Delete a user.
     *
     * @param userId
     * @param principal
     * @param request
     * @throws ResourceNotExistException
     */
    @DeleteMapping("/{user_id}")
    public void deleteUser(@PathVariable("user_id") long userId, Principal principal, HttpServletRequest request)
            throws ResourceNotExistException, InvalidDataException {
        User user = userRepository.findById(userId);

        //User doesn't exist.
        if (user == null) {
            throw new ResourceNotExistException(String.format("No user with id: %s", userId));
        }

        //Non admin can't update to admin role.
        if (!Utils.isPrincipalAdmin(principal) && user.isAdmin()) {
            throw new InvalidDataException("Non admin user can't delete an admin user");
        }

        //We are deleting the user which is logged in.
        if (user.getUsername().equals(principal.getName())) {
            HttpSession session = request.getSession(false);
            SecurityContextHolder.clearContext();
            if (session != null) {
                session.invalidate();
            }
        }

        userRepository.delete(user);
    }

}
