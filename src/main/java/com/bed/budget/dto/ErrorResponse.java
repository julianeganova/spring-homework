package com.bed.budget.dto;

import com.bed.budget.model.InternalErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private InternalErrorStatus status;
    private String message;
}
