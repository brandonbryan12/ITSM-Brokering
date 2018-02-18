package com.ITSMBroker;

import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LookupController {
    
    @RequestMapping("/")
    public String index() {
        return "Use /Lookup with a provider name parameter in the query to receive a service response.";
    }
    
    @RequestMapping(method=GET, value="/Lookup")
    public String Lookup(@RequestParam(value="providerName", defaultValue="None") String providerName){
        System.out.println(providerName);
        return new PayloadPOJO(providerName, PayloadPOJO.ActionType.NONE).getServiceName();
    }
}