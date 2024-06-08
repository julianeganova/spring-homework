package com.bed.budget.model;

import lombok.Data;

import java.util.*;

@Data
public class FamilyBudget {
    private final String name;
    private final Set<String> expenseTypes = new HashSet<>();
    private final Map<Date, MonthsBudget> monthsBudgetList = new HashMap();
    private final Map<Date, MonthsBudget> planMonthsBudgetList = new HashMap<>();
}
