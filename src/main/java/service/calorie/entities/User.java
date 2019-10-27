package service.calorie.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:02
 * Purpose: TODO:
 **/
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    @NotNull
    @Length(min = 5, max = 20)
    private String username;

    @NotNull
    @Length(min = 8)
    private String password;

    // CascadeType.All so that the persistence of parent can do so for children.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

    private UserSetting userSetting;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
        // Setting both sides of the relationship.
        for (UserRole role : roles) {
            role.setUser(this);
        }
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        // Setting both sides of the relationship.
        for (Meal meal : meals) {
            meal.setUser(this);
        }
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        meal.setUser(this);
    }

    public UserSetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

    public boolean isAdmin() {
        for (UserRole role : roles) {
            if (role.getType().equals(UserRole.UserRoleType.ADMIN)) {
                return true;
            }
        }
        return false;
    }
}
