package comstage.stage.controller;

import comstage.stage.DTO.TaskRequest;
import comstage.stage.JwtTokenProvider;
import comstage.stage.entity.Task;
import comstage.stage.service.EmployeeService;
import comstage.stage.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/tasks")
    public ResponseEntity<String> addTask(@RequestBody TaskRequest taskRequest, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request); // Extract token from the request
        String response = taskService.addTask(token, taskRequest);
        if ("Task added successfully".equals(response)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksForLoggedInEmployee(HttpServletRequest request) {
        List<Task> tasks = employeeService.getTasksForLoggedInEmployee(request);
        return ResponseEntity.ok(tasks);
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest updatedTask) {
        String response = taskService.updateTask(taskId, updatedTask);
        if ("Task updated successfully".equals(response)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



}
