package org.dinism.demosecurity.repository;

import org.dinism.demosecurity.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

}