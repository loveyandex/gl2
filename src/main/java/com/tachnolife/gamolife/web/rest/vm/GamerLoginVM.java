package com.tachnolife.gamolife.web.rest.vm;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@Data
@Accessors(fluent = true)
public class GamerLoginVM {


    @NotNull
    @Size(min = 1, max = 50)
    private String phoneNumber;



    @NotNull
    @Size(min = 4, max = 100)
    private String otp;


    private boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }


    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }


}
