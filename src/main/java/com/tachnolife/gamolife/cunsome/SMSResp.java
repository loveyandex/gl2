package com.tachnolife.gamolife.cunsome;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author Abolfazl
 */
@Data
public class SMSResp {

    @JsonProperty("Status")
    private int Status;
    @JsonProperty("Id")
    private Long Id;
    @JsonProperty("WarningMessage")
    private String WarningMessage;
    @JsonProperty("NikIds")
    ArrayList<Long> NikIds = new ArrayList<Long>();


}
