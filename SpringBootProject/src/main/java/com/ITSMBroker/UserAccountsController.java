package com.ITSMBroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Users")
public class UserAccountsController {
    @Autowired
    private CustomerAccountRepository accounts;
    @GetMapping(value = "/read")
    public @ResponseBody ResponseEntity<String> getAccount(@RequestParam(value = "user") String user,
            @RequestParam(value = "pass") String pass, @RequestParam(value = "targetUser") String targetUser){
        CustomerAccount currentUser = accounts.findByUsername(user);
        if(currentUser == null)
            return new ResponseEntity<>("Username for requester not found.", HttpStatus.PRECONDITION_FAILED);
        if(!currentUser.getPassword().equals(pass))
            return new ResponseEntity<>("Requester password invalid.", HttpStatus.PRECONDITION_FAILED);
        if(currentUser.getPermissionsLevel() != PermissionsLevel.ADMIN)
            return new ResponseEntity<>("User account provided has insufficient permissions.", HttpStatus.PRECONDITION_FAILED);
        CustomerAccount requested = accounts.findByUsername(user);
        if(requested == null)
            return new ResponseEntity<>("Requested user account not found.", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(requested.toString(), HttpStatus.OK);
    }
}
