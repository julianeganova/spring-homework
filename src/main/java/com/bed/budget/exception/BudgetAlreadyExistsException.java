package com.bed.budget.exception;

public class BudgetAlreadyExistsException extends RuntimeException{

    public BudgetAlreadyExistsException(String message) {
        super(message);
    }
}
