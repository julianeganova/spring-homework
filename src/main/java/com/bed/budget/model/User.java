package com.bed.budget.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.util.*;

@Getter
@Setter
@Table(name="users")
@Entity
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "users_sequence", allocationSize = 1)
    private int id;
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ExpenseType> expenseTypes;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Filter(name="fact")
    private List<MonthsBudget> monthsBudgetList;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Filter(name="plan")
    private List<MonthsBudget> planMonthsBudgetList;

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", expenseTypes=" + expenseTypes +
                ", monthsBudgetList=" + monthsBudgetList +
                ", planMonthsBudgetList=" + planMonthsBudgetList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
