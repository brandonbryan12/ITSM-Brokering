package com.ITSMBroker;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Organizations")
public class Organization {
    @Id
    private ObjectId id;
    @Indexed(unique=true)
    private String name;
    public Organization(){
        name = "";
    }
    public Organization(String name){
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
        return String.format("Organization[id=%s, name='%s']", id, name);
    }
}
