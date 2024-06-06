package com.bed.budget.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddExpenseRequest {
    private String month;
    private int budgetType;
    private String type;
    private double sum;
    private Date date;
    private String comment;
}
