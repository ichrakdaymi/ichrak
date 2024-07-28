package comstage.stage.service;


import comstage.stage.entity.Admin;
import comstage.stage.entity.Role;
import comstage.stage.repository.AdminRepository;
import comstage.stage.request.SignupAdminRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // private Admin admin4signup;
    public Admin toAdmin(SignupAdminRequest request) {
        Admin admin4signup = new Admin();

        admin4signup.setEmail(request.getEmail());
        admin4signup.setPhoneNumber(request.getPhoneNumber());
        admin4signup.setPassword(request.getPassword());
        admin4signup.setUsername(request.getUsername());
        if (request.getRole()=="ADMIN"){admin4signup.setRole(Role.ADMIN);}

        return admin4signup;
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin saveAdmin(SignupAdminRequest signupAdminRequest) {
        signupAdminRequest.setPassword(passwordEncoder.encode(signupAdminRequest.getPassword()));
        Admin admin = toAdmin(signupAdminRequest);
        String adminUsername = admin.getUsername();
        if (findByUsername(adminUsername).isPresent()){
            throw new IllegalArgumentException("Compte existant ");
        }return  adminRepository.save(admin);
    }
    @PostConstruct
    public void createDefaultAdmin() {
        Admin userADM =new Admin();

        String username = "ADMIN";
        if (!adminRepository.existsByUsername(username)) {
            userADM.setEmail("adm@mail.com");
            userADM.setUsername("ADMIN");
            userADM.setPhoneNumber("1234667");
            userADM.setPassword(passwordEncoder.encode("adm"));
            userADM.setRole(Role.ADMIN);
            adminRepository.save(userADM);
        }


    }

}
