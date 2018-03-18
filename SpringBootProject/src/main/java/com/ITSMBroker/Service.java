package com.ITSMBroker;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Service")
public class Service {
    @Id
    private ObjectId id;
    @Indexed(unique=true)
    private String name;
    public Service(){
        this.name = "";
    }
    public Service(String name){
        this.name = name;
    }
    public ObjectId getID(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return String.format("Service[id=%s, name='%s']", id, name);
    }
}
