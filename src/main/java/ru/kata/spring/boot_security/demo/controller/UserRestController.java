package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private UserServiceImpl userServiceImpl;

    public UserRestController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        List <User> allUsers = userServiceImpl.getAllUsers();
        return allUsers;
    }
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id){
        User user = userServiceImpl.show(id);
        return user;
    }
    @PostMapping("users")
    public User addNewUser(@RequestBody User user){
        userServiceImpl.save(user);
        return user;
    }
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userServiceImpl.update(user.getId(),user);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestBody User user){
        userServiceImpl.delete(user.getId());
        return ResponseEntity.ok(user);
    }
    @GetMapping("/current")
    public ResponseEntity<User> getUserPage(Principal pr) {
        return new ResponseEntity(userServiceImpl.findByUsername(pr.getName()), HttpStatus.OK);
    }
}
