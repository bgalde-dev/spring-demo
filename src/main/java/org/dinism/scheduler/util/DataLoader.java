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
import java.util.Set;

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
        Role role = roleRepository.findByRole("ADMIN");
        if (role == null) {
            role = new Role();
            role.setRole("ADMIN");
            roleRepository.save(role);
        }

        role = roleRepository.findByRole("DRIVER");
        if (role == null) {
            role = new Role();
            role.setRole("DRIVER");
            roleRepository.save(role);
        }

        role = roleRepository.findByRole("SCHEDULER");
        if (role == null) {
            role = new Role();
            role.setRole("SCHEDULER");
            roleRepository.save(role);
        }
    }

    public void loadEmployeeCodes() {
        EmployeeCode code = employeeCodeRepository.findByCode("VIP");
        if (code == null) {
            code = new EmployeeCode();
            code.setCode("VIP");
            code.setCharCode('V');
            code.setDescription("To be used for VIP trips");
            employeeCodeRepository.save(code);
        }

        code = employeeCodeRepository.findByCode("LIMO");
        if (code == null) {
            code = new EmployeeCode();
            code.setCode("LIMO");
            code.setCharCode('L');
            code.setDescription("Can drive stretch non-class B vehicles");
            employeeCodeRepository.save(code);
        }

        code = employeeCodeRepository.findByCode("ANDY");
        if (code == null) {
            code = new EmployeeCode();
            code.setCode("ANDY");
            code.setCharCode('R');
            code.setDescription("Some code that Andy has");
            employeeCodeRepository.save(code);
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
                employee.setSchedule(schedule);

                // TODO: PARSE AND CREATE CODES
                char[] codes = nextLine[2].toUpperCase().toCharArray();
                employee.setCodes(this.employeeCodeParser(codes));

                employeeRepository.save(employee);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Set<EmployeeCode> employeeCodeParser(char[] charCodes) {
        HashSet<EmployeeCode> codes = new HashSet<>();
        for ( char c: charCodes ) {
            EmployeeCode code = employeeCodeRepository.findByCharCode(c);
            if (code != null) {
                codes.add(code);
            }
        }
        return codes;
    }
}
