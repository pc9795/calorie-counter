package service.calorie.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-10-2019 18:06
 * Purpose: Meal entity. Records of meal taken by a user.
 **/
@Entity
@Table(name = "meal_records")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NonNull
    private LocalDate date;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @NotNull
    private LocalTime time;

    // We are using Lob so that there is not a limit of 255 characters.
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @NotNull
    private String text;

    @PositiveOrZero
    private int calories;

    private boolean lessThanExpected;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public boolean isLessThanExpected() {
        return lessThanExpected;
    }

    public void setLessThanExpected(boolean lessThanExpected) {
        this.lessThanExpected = lessThanExpected;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", text='" + text + '\'' +
                ", calories=" + calories +
                ", lessThanExpected=" + lessThanExpected +
                ", user=" + (user == null ? null : user.getId()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meal meal = (Meal) o;

        if (calories != meal.calories) return false;
        if (lessThanExpected != meal.lessThanExpected) return false;
        if (!date.equals(meal.date)) return false;
        if (!time.equals(meal.time)) return false;
        return text.equals(meal.text);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + calories;
        result = 31 * result + (lessThanExpected ? 1 : 0);
        return result;
    }
}
