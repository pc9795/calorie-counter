package service.calorie.controllers;

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
import service.calorie.exceptions.UserAlreadyExistException;
import service.calorie.repositories.UserRepository;
import service.calorie.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 17:57
 * Purpose: TODO:
 **/
@RestController()
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestParam String username, @RequestParam String password)
            throws UserAlreadyExistException {
        if (userRepository.findUserByUsername(username) != null) {
            throw new UserAlreadyExistException();
        }
        User user = new User(username, passwordEncoder.encode(password));
        user.setRoles(Collections.singletonList(new UserRole(UserRole.UserRoleType.REGULAR)));
        userRepository.save(user);
        return user;
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
