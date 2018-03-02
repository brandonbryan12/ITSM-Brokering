package com.ITSMBroker;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CustomerAccount") //unneeded; default behavior is to use class name
public class CustomerAccount {
    @Id
    private String id;
    private String accountUsername, accountPassword, customerName;
    public CustomerAccount(){
        accountPassword = "";
        accountUsername = "";
        customerName = "None";
    }
    public CustomerAccount(String customerName, String accountUsername, String accountPassword){
        this.accountPassword = accountPassword;
        this.accountUsername = accountUsername;
        this.customerName = customerName;
    }
    public String getAccountUsername(){
        return accountUsername;
    }
    public String getAccountPassword(){
        return accountPassword;
    }
    public String getCustomerName(){
        return customerName;
    }
    public void setAccountUsername(String accountUsername){
        this.accountUsername = accountUsername;
    }
    public void setAccountPassword(String accountPassword){
        this.accountPassword = accountPassword;
    }
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }
    @Override
    public String toString() {
        return String.format("CustomerAccount[id=%s, customerName='%s', accountUsername='%s', accountPassword='%s']",
            id, customerName, accountUsername, accountPassword);
    }
}
