package com.bed.budget.repository;

import com.bed.budget.model.MonthsBudget;
import com.bed.budget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BudgetRepository extends JpaRepository<MonthsBudget, Integer> {
    MonthsBudget findByUserAndMonthAndType(User user, Date month, int type);
}
