package com.example.electronicgradebook.controller;

import com.example.electronicgradebook.configuration.security.AuthenticationRequest;
import com.example.electronicgradebook.configuration.security.AuthenticationResponse;
import com.example.electronicgradebook.dto.SpecialStudentsDto;
import com.example.electronicgradebook.dto.StudentAverageGradeDto;
import com.example.electronicgradebook.dto.StudentDto;
import com.example.electronicgradebook.repository.ElectronicGradeBookRepository;
import com.example.electronicgradebook.repository.NewsRepository;
import com.example.electronicgradebook.repository.PrivateMessageRepository;
import com.example.electronicgradebook.resources.News;
import com.example.electronicgradebook.resources.PrivateMessage;
import com.example.electronicgradebook.resources.User;
import com.example.electronicgradebook.util.JwtUtil;
import com.example.electronicgradebook.util.StudentUtil;
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
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ElectronicGradeBookController {

    private final NewsRepository newsRepository;
    private final ElectronicGradeBookRepository electronicGradeBookRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;


    @RequestMapping(path = "/marks-students")
    public ResponseEntity<List<StudentDto>> getMarksForEveryStudent() {
        return ResponseEntity.ok(StudentUtil.getStudentsDto(electronicGradeBookRepository.findAll()));
    }


    @RequestMapping(path = "/marks-student/{email}")
    public ResponseEntity<StudentDto> getMarksForStudent(@PathVariable String email) {
        return ResponseEntity.ok(StudentUtil.getStudentDto(electronicGradeBookRepository.findByEmail(email)));
    }

    @RequestMapping(path = "/students-average-grade")
    public ResponseEntity<List<StudentAverageGradeDto>> getAverageGradeForAllStudents() {
        return ResponseEntity.ok(
                StudentUtil.getListOfAverageGradeForAllStudents(electronicGradeBookRepository.findAll()));
    }

    @RequestMapping(path = "/total-marks")
    public ResponseEntity<Integer> getTotalOfMarks() {
        return ResponseEntity.ok(StudentUtil.getTotalOfMarks(electronicGradeBookRepository.findAll()));
    }

    @RequestMapping(path = "/highest-grade")
    public ResponseEntity<Double> getHighestGrade() {
        return ResponseEntity.ok(StudentUtil.getAverageGradeForStudent(
                StudentUtil.getStudentWithHighestAverageGrade(electronicGradeBookRepository.findAll())));
    }

    @RequestMapping(path = "/special-students")
    public ResponseEntity<SpecialStudentsDto> getSpecialStudents() {
        return ResponseEntity.ok(StudentUtil.getSpecialStudents(electronicGradeBookRepository.findAll()));
    }

    @RequestMapping(path = "/average-class-grade")
    public ResponseEntity<Double> getAverageGradeForClass() {
        return ResponseEntity.ok(StudentUtil.getAverageGradeForClass(electronicGradeBookRepository.findAll()));
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

    @RequestMapping("/students")
    public List<User> getAllStudents() {
        return StudentUtil.getAllStudents(electronicGradeBookRepository.findAll());
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
    public void updateUser(@RequestBody User data) {
        Optional<User> userToBeEdited = electronicGradeBookRepository.findById(data.getId());
        userToBeEdited.ifPresent((user) -> StudentUtil.overrideUserWithNewData(user, data));

        electronicGradeBookRepository.save(userToBeEdited.get());
    }

    @RequestMapping(path = "/news", method = RequestMethod.POST)
    public ResponseEntity<News> addNews(@RequestBody News news) {
        newsRepository.save(news);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @RequestMapping("/news")
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @RequestMapping(path = "news/{id}", method = RequestMethod.DELETE)
    public void deleteNews(@PathVariable long id) {
        newsRepository.deleteById(id);
    }

    @RequestMapping(path = "/message", method = RequestMethod.POST)
    public PrivateMessage addMessage(@RequestBody PrivateMessage message) {
        return privateMessageRepository.save(message);
    }

    @RequestMapping(path = "/message/{email}", method = RequestMethod.GET)
    public List<PrivateMessage> getMessagesByReceiver(@PathVariable String email) {
        return privateMessageRepository.findByReceiver(email);
    }

    @RequestMapping(path = "message/{id}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable long id) {
        privateMessageRepository.deleteById(id);
    }

}
