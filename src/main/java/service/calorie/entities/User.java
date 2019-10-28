package service.calorie.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:02
 * Purpose: User entity
 **/
@Entity
@Table(name = "users")
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

    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @NotNull
    @NotEmpty
    @Valid
    private List<UserRole> roles = new ArrayList<>();

    // Meal is a week entity so enabling orphan removal.
    // CascadeType.All so that the persistence of parent can do so for children.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

    @Embedded
    @NotNull
    private UserSettings userSettings;

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
    }

    @JsonProperty
    public List<Meal> getMeals() {
        return meals;
    }

    @JsonIgnore
    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        // Setting both sides of the relationship.
        for (Meal meal : meals) {
            meal.setUser(this);
        }
    }

    public void addMeal(Meal meal) {
        // Setting both sides of the relationship.
        this.meals.add(meal);
        meal.setUser(this);
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    /**
     * Check if user has an admin role.
     *
     * @return true if has admin role.
     */
    @JsonIgnore
    public boolean isAdmin() {
        for (UserRole role : roles) {
            if (role.getType().equals(UserRole.UserRoleType.ADMIN)) {
                return true;
            }
        }
        return false;
    }
}
