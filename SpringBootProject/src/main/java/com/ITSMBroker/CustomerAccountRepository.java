package com.ITSMBroker;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerAccountRepository extends MongoRepository<CustomerAccount, String> {
    CustomerAccount findByCustomerName(String customerName);
}
