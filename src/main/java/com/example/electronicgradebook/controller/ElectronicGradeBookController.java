package com.example.electronicgradebook.controller;

import com.example.electronicgradebook.configuration.security.AuthenticationRequest;
import com.example.electronicgradebook.configuration.security.AuthenticationResponse;
import com.example.electronicgradebook.repository.ElectronicGradeBookRepository;
import com.example.electronicgradebook.resources.User;
import com.example.electronicgradebook.util.JwtUtil;
import com.example.electronicgradebook.util.MathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ElectronicGradeBookController {

    private final ElectronicGradeBookRepository electronicGradeBookRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @RequestMapping(path="/average-class-grade")
    public ResponseEntity<Double> getAverageGradeForClass() {
        return ResponseEntity.ok(MathUtil.getAverageGradeForClass(electronicGradeBookRepository.findAll()));
    }
    @RequestMapping(path = "/students-total")
    public ResponseEntity<Integer> getNumberOfEnrolledStudents() {
        List<User> listOfAllUsers = electronicGradeBookRepository.findAll();
        return ResponseEntity.ok(listOfAllUsers.size() - 1);
    }

    @RequestMapping(path = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("Wrong password");
            return new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        String role;
        if (userDetails.getUsername().equals("admin")) {
            role = "admin";
        } else {
            role = "user";
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt, role));
    }

    @RequestMapping("/users")
    public List<User> getUsers() {
        return electronicGradeBookRepository.findAll();
    }

    @RequestMapping(path = "users/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable long id) {
        electronicGradeBookRepository.deleteById(id);
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (electronicGradeBookRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("User already exists in database...");
            return new ResponseEntity<>(new User(), HttpStatus.OK);
        }
        electronicGradeBookRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable long id, @RequestBody User user) {
        electronicGradeBookRepository.save(user);
    }
}
