package comstage.stage.service;

import comstage.stage.DTO.EmployeeRequest;
import comstage.stage.entity.Employee;
import comstage.stage.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addEmployee(EmployeeRequest employee) {
        // Encode le mot de passe avant de l'enregistrer

        if ((employeeRepository.findByUsername(employee.getUsername())).isPresent()){
            return "Employee exists";
        }else {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
//         Enregistre l'employé dans la base de données
            Employee emp = new Employee();
            emp.setUsername(employee.getUsername());
            emp.setPassword(employee.getPassword());
            emp.setFirstName(employee.getFirstName());
            emp.setLastName(employee.getLastName());
            emp.setEmail(employee.getEmail());
            emp.setPhoneNumber(employee.getPhoneNumber());
        employeeRepository.save(emp);
    }
    return "Employee added successfully";}
}
