package com.ITSMBroker;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerAccountRepository extends MongoRepository<CustomerAccount, ObjectId> {
    CustomerAccount findById(ObjectId id);
    CustomerAccount findByUsername(String username);
    List<CustomerAccount> findByOrganization(Organization org);
}
