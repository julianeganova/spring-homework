package com.bed.budget.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonthsBudget
{
    public static final int PLAN = 0;
    public static final int FACT = 1;
    private final int type;
    private final List<Expense> expenses = new ArrayList<>();
}
