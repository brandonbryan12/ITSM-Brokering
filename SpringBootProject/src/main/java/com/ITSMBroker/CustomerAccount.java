package com.ITSMBroker;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CustomerAccount")
public class CustomerAccount {
    @Id
    private ObjectId id;
    @DBRef
    private Organization org;
    @DBRef
    private Service service;
    @Indexed(unique=true)
    private String username;
    private String password, serviceUsername, servicePassword;
    private PermissionsLevel permissionsLevel;
    public CustomerAccount(){
        password = "";
        username = "";
        org = new Organization();
        service = new Service();
        serviceUsername = "";
        servicePassword = "";
    }
    public CustomerAccount(Organization org, Service service, String username, String password, String serviceUsername, String servicePassword,
            PermissionsLevel permissionsLevel){
        this.org = org;
        this.service = service;
        this.username = username;
        this.password = password;
        this.serviceUsername = serviceUsername;
        this.servicePassword = servicePassword;
        this.permissionsLevel = permissionsLevel;
    }
    public ObjectId getID(){
        return id;
    }
    public Organization getOrganization(){
        return org;
    }
    public Service getService(){
        return service;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getServiceUsername(){
        return serviceUsername;
    }
    public String getServicePassword(){
        return servicePassword;
    }
    public PermissionsLevel getPermissionsLevel(){
        return permissionsLevel;
    }
    public void setOrganization(Organization org){
        this.org = org;
    }
    public void setService(Service service){
        this.service = service;
    }
    public void setUsername(String accountUsername){
        this.username = accountUsername;
    }
    public void setPassword(String accountPassword){
        this.password = accountPassword;
    }
    public void setServiceUsername(String serviceUsername){
        this.serviceUsername = serviceUsername;
    }
    public void setServicePassword(String servicePassword){
        this.servicePassword = servicePassword;
    }
    public void setPermissionsLevel(PermissionsLevel permissionsLevel){
        this.permissionsLevel = permissionsLevel;
    }
    @Override
    public String toString() {
        return String.format("CustomerAccount[id=%s, orgId=%s, serviceId=%s, username='%s', password='%s', serviceUsername=%s, servicePassword='%s'"
                + ", permissionsLevel=%s]",
            id, org.getID(), service.getID(), username, password, serviceUsername, servicePassword, permissionsLevel);
    }
}
