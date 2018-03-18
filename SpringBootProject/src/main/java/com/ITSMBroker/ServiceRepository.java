package com.ITSMBroker;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<Service, ObjectId> {
    Service findByID(ObjectId id);
    Service findByName(String name);
}
