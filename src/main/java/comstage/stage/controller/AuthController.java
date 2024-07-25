package comstage.stage.controller;

import comstage.stage.JwtTokenProvider;
import comstage.stage.request.AuthRequest;
import comstage.stage.request.SignupAdminRequest;
import comstage.stage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AdminService adminService;

    @PostMapping("/sign-up/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody SignupAdminRequest admin) {
        try {
            adminService.saveAdmin(admin);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin registered successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {            //accepts login credentials (username and password)
        try {
            Authentication authentication = authenticationManager.authenticate(                 //authenticate the user based on the provided credentials using authenticationManager
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));  // creates a UsernamePasswordAuthenticationToken with the provided username and password, and passes it to the authenticate method of the AuthenticationManager

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.createToken(                                    //If authentication is successful, we generate a JWT token using the JwtTokenProvider:
                    authRequest.getUsername(), authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));   //If the authentication is successful, the AuthenticationManager returns an Authentication object. The controller then uses the JwtTokenProvider to generate a JWT token for the authenticated user. This token includes the user's username and roles.
            Map<String, String> response = new HashMap<>();
            response.put("token", token); // username + role
            System.out.println("token------- " + response);
            return ResponseEntity.ok(response);                            //The controller returns a ResponseEntity containing the JWT token to the client
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

        }
    }

}