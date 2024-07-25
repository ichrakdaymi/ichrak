package comstage.stage.controller;
import comstage.stage.DTO.EmployeeRequest;
import comstage.stage.JwtTokenProvider;
import comstage.stage.entity.Employee;
import comstage.stage.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeRequest employee) {

            employeeService.addEmployee(employee);
            return ResponseEntity.ok("Employee added successfully");
    }
}
