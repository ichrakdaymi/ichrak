package comstage.stage.service;

import comstage.stage.DTO.TaskRequest;
import comstage.stage.entity.Employee;
import comstage.stage.entity.Task;
import comstage.stage.repository.EmployeeRepository;
import comstage.stage.repository.TaskRepository;
import comstage.stage.JwtTokenProvider; // Import the JwtTokenProvider class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Correctly autowired

    public String addTask(String token, TaskRequest taskRequest) {
        Long employeeId = jwtTokenProvider.getUserIdFromToken(token);

        if (employeeId == null) {
            return "Employee ID not found in token";
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));

        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setCreationDate(new Date());
        task.setEmployee(employee);

        // Save the task
        taskRepository.save(task);

        return "Task added successfully";
    }
    public String updateTask(Long taskId, TaskRequest updatedTask) {
        // Find the task by ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if the task can be updated
        if (!canUpdateTask(task)) {
            return "Task can only be updated on the day it was created";
        }

        // Update the task fields
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());

        // Save the updated task
        taskRepository.save(task);

        return "Task updated successfully";
    }

    // Method to check if the task can be updated
    private boolean canUpdateTask(Task task) {
        Date creationDate = task.getCreationDate();
        Date today = new Date();

        // Set the time portion of the creation date and today's date to midnight for comparison
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(creationDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(today);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        // Check if the creation date is equal to today's date
        return cal1.getTime().equals(cal2.getTime());
    }

    public List<Task> getTasksByUser(String username) {
        return taskRepository.findByEmployeeUsername(username); // Implement this method in your repository
    }
}
