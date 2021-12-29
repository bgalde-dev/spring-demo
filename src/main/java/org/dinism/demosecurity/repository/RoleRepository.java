package org.dinism.demosecurity.repository;

import org.dinism.demosecurity.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}