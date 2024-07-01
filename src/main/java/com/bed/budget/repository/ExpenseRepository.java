package com.bed.budget.repository;

import com.bed.budget.model.Expense;
import com.bed.budget.model.MonthsBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository <Expense, Integer> {
    List<Expense> findByBudget(MonthsBudget budget);
}
