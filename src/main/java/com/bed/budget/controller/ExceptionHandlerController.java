package com.bed.budget.controller;

import com.bed.budget.dto.ErrorResponse;
import com.bed.budget.exception.BudgetAlreadyExistsException;
import com.bed.budget.exception.NoCurrentBudgetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.bed.budget.model.InternalErrorStatus.DUBLICATE_BUDGET;
import static com.bed.budget.model.InternalErrorStatus.NO_CURRENT_BUDGET;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NoCurrentBudgetException.class)
    public ResponseEntity<ErrorResponse> handleNoCurrentBudgetException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(NO_CURRENT_BUDGET, e.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BudgetAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBudgetAlreadyExistsException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(DUBLICATE_BUDGET, e.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
