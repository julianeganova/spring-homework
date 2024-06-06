package com.bed.budget.model;

import lombok.Data;

import java.util.Date;

@Data
public class Expense {
    private final String expenseType;
    private final double sum;
    private final Date date;
    private final String comment;
}
