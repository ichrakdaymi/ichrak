package comstage.stage.request;

import comstage.stage.entity.Role;
import comstage.stage.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class DETALLEmp {
    //private Long id;

    private String email;
    private String phoneNumber;
    private String username;
    private Role role;                 // to verify
    private List<Task> tasks;
}
