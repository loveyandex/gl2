package com.tachnolife.gamolife.web.rest.payload.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import liquibase.pro.packaged.S;

/**
 * @author Abolfazl
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sending {

    String username = "Your UserName";
    String password = "Your Password";
    String ptpTestMobile ="Your Ptp Test Mobile";

}
