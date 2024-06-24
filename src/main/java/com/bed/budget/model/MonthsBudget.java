package com.bed.budget.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="budgets")
@NoArgsConstructor
@FilterDef(name="plan", defaultCondition="type = 0")
@FilterDef(name="fact", defaultCondition="type = 1")
public class MonthsBudget
{
    public static final int PLAN = 0;
    public static final int FACT = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "budget_seq")
    @SequenceGenerator(name = "budget_seq", sequenceName = "budgets_sequence", allocationSize = 1)
    private int id;
    private int type;
    private Date month;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy="budget", fetch = FetchType.EAGER)
    private List<Expense> expenses;
    public MonthsBudget (User user, Date month, int type) {
        this.user = user;
        this.month = month;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MonthsBudget{" +
                "id=" + id +
                ", type=" + type +
                ", month=" + month +
                ", user=" + user +
                ", expenses=" + expenses +
                '}';
    }
}
