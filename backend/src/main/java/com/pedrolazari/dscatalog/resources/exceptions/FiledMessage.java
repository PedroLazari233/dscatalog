package com.pedrolazari.dscatalog.resources.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FiledMessage implements Serializable {

    private String fieldName;
    private String message;

    public FiledMessage() {
    }
}
