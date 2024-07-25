package comstage.stage.service;


import comstage.stage.entity.Admin;
import comstage.stage.entity.Role;
import comstage.stage.repository.AdminRepository;
import comstage.stage.request.SignupAdminRequest;
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
        admin4signup.setFirstName(request.getFirstName());
        admin4signup.setLastName(request.getLastName());
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

}
