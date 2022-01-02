package org.dinism.scheduler.repository;

import org.dinism.scheduler.model.EmployeeCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeCodeRepository  extends MongoRepository<EmployeeCode, String> {

    EmployeeCode findByCode(String code);
}
