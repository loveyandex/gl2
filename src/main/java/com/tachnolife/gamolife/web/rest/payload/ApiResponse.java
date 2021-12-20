package com.tachnolife.gamolife.web.rest.payload;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author Abolfazl
 */
public class ApiResponse {

    public String status;

    public ApiResponse(String status) {
        this.status = status;
    }
}
