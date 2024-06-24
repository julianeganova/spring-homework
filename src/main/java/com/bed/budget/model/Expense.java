package com.bed.budget.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="expenses")
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "budget_seq")
    @SequenceGenerator(name = "budget_seq", sequenceName = "budgets_sequence", allocationSize = 1)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type", referencedColumnName = "id")
    private ExpenseType expenseType;
    private double sum;
    @Column(name = "date_of_expense")
    private Date date;
    private String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "budget_id", referencedColumnName = "id")
    private MonthsBudget budget;
    public Expense(ExpenseType expenseType, double sum, Date date, String comment, MonthsBudget budget) {
        this.expenseType = expenseType;
        this.sum = sum;
        this.date = date;
        this.comment = comment;
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", expenseType=" + expenseType.getName() +
                ", sum=" + sum +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", budget=" + budget.getMonth() +
                '}';
    }
}
