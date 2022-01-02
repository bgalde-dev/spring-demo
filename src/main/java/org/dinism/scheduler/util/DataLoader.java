package org.dinism.scheduler.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.dinism.scheduler.model.Employee;
import org.dinism.scheduler.model.EmployeeCode;
import org.dinism.scheduler.model.Role;
import org.dinism.scheduler.repository.EmployeeCodeRepository;
import org.dinism.scheduler.repository.EmployeeRepository;
import org.dinism.scheduler.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

@Component
@EnableMongoRepositories
public class DataLoader  implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeCodeRepository employeeCodeRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {

        this.loadRoles();
        this.loadEmployeeCodes();
        this.loadEmployees();

    }

    public void loadRoles() {
        // ADD ROLES
        Role adminRole = roleRepository.findByRole("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setRole("ADMIN");
            roleRepository.save(adminRole);
        }

        Role userRole = roleRepository.findByRole("DRIVER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setRole("DRIVER");
            roleRepository.save(userRole);
        }
    }

    public void loadEmployeeCodes() {
        EmployeeCode vipCode = employeeCodeRepository.findByCode("VIP");
        if (vipCode == null) {
            vipCode = new EmployeeCode();
            vipCode.setCode("VIP");
            vipCode.setDescription("To be used for VIP trips");
            employeeCodeRepository.save(vipCode);
        }
    }

    public void loadEmployees() {
        // Clean up any previous data
        employeeRepository.deleteAll(); // Doesn't delete the collection

        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            File resource = new ClassPathResource("assets/employees-01.csv").getFile();
            FileReader filereader = new FileReader(resource);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();

            String [] nextLine;

            Role driverRole = roleRepository.findByRole("DRIVER");

            while ((nextLine = csvReader.readNext()) != null) {

                Employee employee =new Employee();

                employee.setEmployeeId(nextLine[0]);
                employee.setName(nextLine[1]);
                employee.setSeniority(Integer.parseInt(nextLine[4]));
                employee.setStatus(nextLine[5]);
                employee.setLicenseClass(nextLine[3]);
                employee.setLocation(nextLine[6]);
                employee.setStartLocation(nextLine[7]);
                employee.setPersonalPhone(nextLine[16]);
                employee.setWorkPhone(nextLine[15]);
                employee.setPassword(bCryptPasswordEncoder.encode("1234"));
                employee.setRoles(new HashSet<>(Arrays.asList(driverRole)));

                // TODO: CREATE BASE SCHEDULE
                Hashtable<DayOfWeek,String> schedule = new Hashtable<>();
                schedule.put(DayOfWeek.SUNDAY,nextLine[8]);
                schedule.put(DayOfWeek.MONDAY,nextLine[9]);
                schedule.put(DayOfWeek.TUESDAY,nextLine[10]);
                schedule.put(DayOfWeek.WEDNESDAY,nextLine[11]);
                schedule.put(DayOfWeek.THURSDAY,nextLine[12]);
                schedule.put(DayOfWeek.FRIDAY,nextLine[13]);
                schedule.put(DayOfWeek.SATURDAY,nextLine[14]);

                // TODO: PARSE AND CREATE CODES
                String codes = nextLine[2];

                employeeRepository.save(employee);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
