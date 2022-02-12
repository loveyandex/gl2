package com.tachnolife.gamolife.cunsome;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "sms-client", url = "https://niksms.com/fa/publicapi/groupsms",
    configuration = CoreFeignConfiguration.class)
public interface SMSClient {


    @RequestMapping(value = "/business", method = POST,
        consumes = "application/json")
    SMSResp sendPtoPsms(Map<String, ?> smsParams);


}
