package com.bed.budget.exception;

public class NoCurrentBudgetException extends RuntimeException {
    public NoCurrentBudgetException(String message) {
        super(message);
    }
}
