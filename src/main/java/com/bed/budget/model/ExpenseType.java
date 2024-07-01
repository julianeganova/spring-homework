package com.bed.budget.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
@Getter
@Setter
@Entity
@Table(name="expense_types")
@NoArgsConstructor
public class ExpenseType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exp_type_seq")
    @SequenceGenerator(name = "exp_type_seq", sequenceName = "expense_types_sequence", allocationSize = 1)
    private Integer id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    public ExpenseType(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @Override
    public String toString() {
        return "ExpenseType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user.getName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseType that = (ExpenseType) o;
        return Objects.equals(name, that.name) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, user);
    }
}
