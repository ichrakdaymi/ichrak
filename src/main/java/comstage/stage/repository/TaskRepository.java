package comstage.stage.repository;

import comstage.stage.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEmployeeId(Long employeeId);
    List<Task> findByEmployeeUsername(String username);

}
