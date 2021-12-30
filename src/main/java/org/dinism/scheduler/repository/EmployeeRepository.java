package org.dinism.scheduler.repository;

import org.dinism.scheduler.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Employee findByEmail(String email);

}