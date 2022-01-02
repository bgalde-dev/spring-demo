package org.dinism.scheduler;

import org.dinism.scheduler.repository.EmployeeCodeRepository;
import org.dinism.scheduler.repository.EmployeeRepository;
import org.dinism.scheduler.repository.RoleRepository;
import org.dinism.scheduler.util.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class DemoSecurityApplication implements CommandLineRunner {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeCodeRepository employeeCodeRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoSecurityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        DataLoader dataLoader = new DataLoader();
        dataLoader.loadRoles();
        dataLoader.loadEmployees();

    }

}
