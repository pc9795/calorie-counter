package service.calorie.beans;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import service.calorie.entities.User;
import service.calorie.entities.UserRole;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 02:00
 * Purpose: TODO:
 **/
public class ApiUserPrincipal implements UserDetails {
    private User user;

    public ApiUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new LinkedList<>();
        for (UserRole role : user.getRoles()) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getType().toString()));
        }
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
