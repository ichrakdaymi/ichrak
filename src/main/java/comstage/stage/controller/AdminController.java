package comstage.stage.controller;

import comstage.stage.DTO.EmployeeRequest;
import comstage.stage.entity.Employee;
import comstage.stage.repository.EmployeeRepository;
import comstage.stage.request.DETALLEmp;
import comstage.stage.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;



    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeRequest employee) {

        employeeService.addEmployee(employee);
        return ResponseEntity.ok("Employee added successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employeeRepository.deleteById(id);
                    return ResponseEntity.ok("Employee deleted");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/employees")
    public List<DETALLEmp> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeService.convertToDTOs(employees);
    }


}
