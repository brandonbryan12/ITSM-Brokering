package com.ITSMBroker;

public class PayloadPOJO {

    public enum ActionType{
        NONE,
        CREATE,
        UPDATE,
        DELETE
    }

    private String providerName, serviceName;
    private ActionType actionType;

    public PayloadPOJO(){}
    public PayloadPOJO(String providerName, ActionType actionType){
        this.providerName = providerName;
        this.actionType = actionType;
        this.serviceName = TranslateProviderToServiceName(providerName);
    }

    public String getProviderName(){
        return providerName;
    }

    public void setProviderName(String providerName){
        this.providerName = providerName;
    }

    public ActionType getActionType(){
        return actionType;
    }

    public void setActionType(ActionType actionType){
        this.actionType = actionType;
    }

    public String getServiceName(){
        return serviceName;
    }

    private String TranslateProviderToServiceName(String providerName){
        switch (providerName) {
            case "Walmart":
                return "sn";
            case "Target":
                return "sf";
            default:
                return "NO_SERVICE_FOUND_INVALID_PROVIDER";
        }
    }
}
