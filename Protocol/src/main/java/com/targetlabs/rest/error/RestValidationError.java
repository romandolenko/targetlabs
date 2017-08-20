package com.targetlabs.rest.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dolenko Roman <dolenko.roman@gmail.com> on 20.08.2017.
 */
public class RestValidationError implements Serializable{

    private List<RestFieldError> restFieldErrors = new ArrayList<>();

    public RestValidationError() {

    }

    public void addFieldError(String path, String message) {
        RestFieldError error = new RestFieldError(path, message);
        restFieldErrors.add(error);
    }

    public List<RestFieldError> getRestFieldErrors() {
        return restFieldErrors;
    }

    public void setRestFieldErrors(List<RestFieldError> restFieldErrors) {
        this.restFieldErrors = restFieldErrors;
    }
}
