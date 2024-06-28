package com.bed.budget.repository;

import com.bed.budget.model.ExpenseType;
import com.bed.budget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Integer> {
    ExpenseType findByUserAndName(User user, String name);
    List<ExpenseType> findByUser(User user);
}
