package com.oauth2.security.auth;

import com.oauth2.security.config.JwtService;
import com.oauth2.security.user.Role;
import com.oauth2.security.user.User;
import com.oauth2.security.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        String email = request.getEmail();
        String firstname = request.getFirstname();
        String lastname = request.getLastname();
        String password = request.getPassword();
        String generatedPassword = passwordEncoder.encode(password);
        Role role = Role.USER;

        User user = new User(
                request.getEmail(),
                request.getFirstname(),
                request.getLastname(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER);

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail()).
                orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
