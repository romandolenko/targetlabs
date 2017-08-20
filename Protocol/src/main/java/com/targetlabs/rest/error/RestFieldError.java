package com.targetlabs.rest.error;

import java.io.Serializable;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 20.08.2017.
 */
public class RestFieldError implements Serializable {

    private String field;

    private String message;

    public RestFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
