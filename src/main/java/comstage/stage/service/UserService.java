package comstage.stage.service;

import comstage.stage.entity.Admin;
import comstage.stage.entity.Employee;
import comstage.stage.entity.Role;
import comstage.stage.repository.AdminRepository;
import comstage.stage.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    public Optional<Long> findUserIdByUsername(String username) {
        // Try to find the user as Admin
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return Optional.of(admin.get().getId());
        }

        // Try to find the user as Employee
        Optional<Employee> employee = employeeRepository.findByUsername(username);
        if (employee.isPresent()) {
            return Optional.of(employee.get().getId());
        }

        // Return an empty Optional if the user is not found
        return Optional.empty();
    }

}
