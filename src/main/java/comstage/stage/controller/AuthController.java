package comstage.stage.controller;

import comstage.stage.JwtTokenProvider;
import comstage.stage.request.AuthRequest;
import comstage.stage.request.SignupAdminRequest;
import comstage.stage.service.AdminService;
import comstage.stage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService; // Inject UserService


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Assuming you have a method to get the user ID from the username
            Long userId = getUserIdFromUsername(authRequest.getUsername());

            String token = jwtTokenProvider.createToken(
                    authRequest.getUsername(),
                    userId,
                    authentication.getAuthorities().stream()
                            .map(grantedAuthority -> grantedAuthority.getAuthority())
                            .collect(Collectors.toList())
            );

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    private Long getUserIdFromUsername(String username)   {
        Optional<Long> optionalUserId = userService.findUserIdByUsername(username);
        return optionalUserId.orElseThrow(() -> new RuntimeException("User not found"));
    }

}
