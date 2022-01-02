package org.dinism.scheduler.service;

import org.dinism.scheduler.model.Employee;
import org.dinism.scheduler.model.Role;
import org.dinism.scheduler.repository.EmployeeCodeRepository;
import org.dinism.scheduler.repository.EmployeeRepository;
import org.dinism.scheduler.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomEmployeeDetailsService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeCodeRepository employeeCodeRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Employee findUserByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public void saveUser(Employee employee) {
        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
        employee.setEnabled(true);
        Role userRole = roleRepository.findByRole("ADMIN");
        employee.setRoles(new HashSet<>(Arrays.asList(userRole)));
        employeeRepository.save(employee);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmail(email);
        if(employee != null) {
            List<GrantedAuthority> authorities = getUserAuthority(employee.getRoles());
            return buildUserForAuthentication(employee, authorities);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails buildUserForAuthentication(Employee employee, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(), authorities);
    }

}
