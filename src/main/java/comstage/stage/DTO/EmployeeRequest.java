package comstage.stage.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private String username;
}
