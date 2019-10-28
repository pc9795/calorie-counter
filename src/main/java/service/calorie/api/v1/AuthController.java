package service.calorie.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import service.calorie.entities.User;
import service.calorie.entities.UserRole;
import service.calorie.entities.UserSettings;
import service.calorie.exceptions.UserAlreadyExistException;
import service.calorie.repositories.UserRepository;
import service.calorie.utils.Constants;
import service.calorie.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 17:57
 * Purpose: Controller for authentication
 **/
@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, Validator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestParam String username, @RequestParam String password)
            throws UserAlreadyExistException {
        if (userRepository.findUserByUsername(username) != null) {
            throw new UserAlreadyExistException();
        }
        User user = new User(username.trim(), password.trim());
        user.setRoles(Collections.singletonList(new UserRole(UserRole.UserRoleType.REGULAR)));

        //Check for any violations in constraints.
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ValidationException(Utils.joinCollection(violations));
        }

        user.setUserSettings(new UserSettings(0));
        //Encoding after checking validation
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(HttpServletRequest request, @RequestParam String username, @RequestParam String password)
            throws ServletException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException(Constants.ErrorMsg.BAD_CREDENTIALS);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(Constants.ErrorMsg.BAD_CREDENTIALS);
        }
        request.login(username, password);
        return user;
    }
}
