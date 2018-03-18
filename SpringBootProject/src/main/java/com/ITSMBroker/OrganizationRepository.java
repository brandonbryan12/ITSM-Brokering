package com.ITSMBroker;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizationRepository extends MongoRepository<Organization, ObjectId> {
    Organization findByID(ObjectId id);
    Organization findByName(String name);
}
