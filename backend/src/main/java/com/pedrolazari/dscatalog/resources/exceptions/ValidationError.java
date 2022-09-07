package com.pedrolazari.dscatalog.resources.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ValidationError extends StandardError{

    @Getter
    private List<FiledMessage> errors = new ArrayList<>();

    public void addError(String fieldName, String message){
        errors.add(new FiledMessage(fieldName, message));
    }

    public ValidationError() {
    }
}
