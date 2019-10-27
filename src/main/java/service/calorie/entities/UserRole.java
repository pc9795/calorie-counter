package service.calorie.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created By: Prashant Chaubey
 * Created On: 26-10-2019 01:43
 * Purpose: TODO:
 **/
@Entity(name = "user_roles")
public class UserRole {
    public enum UserRoleType {
        REGULAR, ADMIN, USER_MANAGER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole.UserRoleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserRole() {
    }

    public UserRole(UserRoleType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserRoleType getType() {
        return type;
    }

    public void setType(UserRoleType type) {
        this.type = type;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }
}
