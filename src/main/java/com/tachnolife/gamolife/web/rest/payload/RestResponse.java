package com.tachnolife.gamolife.web.rest.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 1/29/2021 , 8:14 PM.
 */

public class RestResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    public RestResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
