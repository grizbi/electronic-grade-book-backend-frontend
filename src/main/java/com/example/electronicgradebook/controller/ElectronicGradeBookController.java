package com.example.electronicgradebook.controller;

import com.example.electronicgradebook.repository.ElectronicGradeBookRepository;
import com.example.electronicgradebook.resources.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ElectronicGradeBookController {

    private final ElectronicGradeBookRepository electronicGradeBookRepository;

    @RequestMapping("/users")
    public List<User> getUsers() {
        return electronicGradeBookRepository.findAll();
    }

    @RequestMapping(
            path = "users/{id}",
            method = RequestMethod.DELETE
    )
    public void deleteUser(@PathVariable long id) {
        electronicGradeBookRepository.deleteById(id);
    }

    @RequestMapping(
            path = "/users",
            method = RequestMethod.POST
    )
    public void addUser(@RequestBody User user) {
        electronicGradeBookRepository.save(user);
    }

    @RequestMapping(
            path = "/users/{id}",
            method = RequestMethod.PUT
    )
    public void updateUser(@PathVariable long id, @RequestBody User user) {
        electronicGradeBookRepository.save(user);
    }
}
