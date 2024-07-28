package comstage.stage.service;

import comstage.stage.DTO.EmployeeRequest;
import comstage.stage.JwtTokenProvider;
import comstage.stage.entity.Employee;
import comstage.stage.entity.Task;
import comstage.stage.repository.EmployeeRepository;
import comstage.stage.repository.TaskRepository;
import comstage.stage.request.DETALLEmp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
//add
    public String addEmployee(EmployeeRequest employee) {

        if ((employeeRepository.findByUsername(employee.getUsername())).isPresent()){
            return "Employee exists";
        }else {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            Employee emp = new Employee();
            emp.setUsername(employee.getUsername());
            emp.setPassword(employee.getPassword());
            emp.setEmail(employee.getEmail());
            emp.setPhoneNumber(employee.getPhoneNumber());
        employeeRepository.save(emp);
    }
    return "Employee added successfully";
    }

    public DETALLEmp convertToDTO(Employee employee) {
        DETALLEmp dto = new DETALLEmp();
      // dto.setId(employee.getId());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setUsername(employee.getUsername());
        dto.setRole(employee.getRole());
        dto.setTasks(employee.getTasks());
        return dto;
    }
    @GetMapping("/employees")
    public List<DETALLEmp> convertToDTOs(List<Employee> employees) {
        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


//delete
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    public List<Task> getTasksForEmployee(Long employeeId) {
        return taskRepository.findByEmployeeId(employeeId);
    }
    public List<Task> getTasksForLoggedInEmployee(HttpServletRequest request) {
        // Extract the token from the request
        String token = jwtTokenProvider.resolveToken(request);

        // Extract user ID from the token
        Long employeeId = jwtTokenProvider.getUserIdFromToken(token);

        // Fetch tasks for the employee
        return taskRepository.findByEmployeeId(employeeId);
    }}

