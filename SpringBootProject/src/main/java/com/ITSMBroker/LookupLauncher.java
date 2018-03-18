package com.ITSMBroker;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LookupLauncher implements CommandLineRunner {
    @Autowired
    private CustomerAccountRepository repository;
    
    public static void main(String[] args) { SpringApplication.run(LookupLauncher.class, args); }

    @Override
    public void run(String... strings) throws Exception {
		/*repository.deleteAll();
		repository.save(new CustomerAccount("Walmart", "walmartuser", "walmartpass"));
		repository.save(new CustomerAccount("Target", "targetuser", "targetpass"));
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (CustomerAccount customer : (List<CustomerAccount>)repository.findAll())
			System.out.println(customer);
		System.out.println();
		System.out.println("Customer found with findByCustomerName:");
		System.out.println("--------------------------------");
		System.out.println(repository.findByCustomerName("Walmart"));
		System.out.println(repository.findByCustomerName("Target"));*/
    }
}
