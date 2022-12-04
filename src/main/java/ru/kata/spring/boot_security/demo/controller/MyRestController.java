package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserSecurityService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {
    private UserService userService;
    private UserSecurityService userSecurityService;
    @Autowired
    public MyRestController(UserService userService, UserSecurityService userSecurityService) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        List <User> allUsers = userService.getAllUsers();
        return allUsers;
    }
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id){
        User user = userService.show(id);
        return user;
    }
    @PostMapping("users")
    public User addNewUser(@RequestBody User user){
        userService.save(user);
        return user;
    }
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userService.update(user.getId(),user);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestBody User user){
        userService.delete(user.getId());
        return ResponseEntity.ok(user);
    }
    @GetMapping("/current")
    public ResponseEntity<User> getUserPage(Principal pr) {
        return new ResponseEntity(userSecurityService.findByUsername(pr.getName()), HttpStatus.OK);
    }
}
