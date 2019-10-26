package service.calorie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import service.calorie.beans.ApiUserPrincipal;
import service.calorie.entities.User;
import service.calorie.repositories.UserRepository;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 01:57
 * Purpose: TODO:
 **/
@Service
public class ApiUserDetailsService implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public ApiUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new ApiUserPrincipal(user);
    }
}
