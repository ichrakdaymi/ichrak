package comstage.stage;


import comstage.stage.entity.Role;
import comstage.stage.repository.AdminRepository;
import comstage.stage.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {
            @Autowired
        private EmployeeRepository employeeRepository ;

        @Autowired
        private AdminRepository adminRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserDetails userDetails;

            userDetails = employeeRepository.findByUsername(username)
                    .map(employee -> new org.springframework.security.core.userdetails.User(employee.getUsername(), employee.getPassword(),
                            getAuthorities(employee.getRole())))
                    .orElse(null);


            if (userDetails == null) {
                userDetails = adminRepository.findByUsername(username)
                        .map(admin -> new org.springframework.security.core.userdetails.User(admin.getUsername(), admin.getPassword(),
                                getAuthorities(admin.getRole())))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            }

            return userDetails;
        }

        private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            return authorities;
        }
    }




