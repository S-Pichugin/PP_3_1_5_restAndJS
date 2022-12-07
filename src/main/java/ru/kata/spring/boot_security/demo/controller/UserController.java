package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping(value = "/user")
    public String userPage (Principal principal, ModelMap model) {
        User user = userServiceImpl.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }
    @GetMapping(value = "/admin")
    public String print(Principal principal,ModelMap model) {
        User user = userServiceImpl.findByUsername(principal.getName());
        User userEmpty = new User();
        model.addAttribute("userEmpty", userEmpty);
        model.addAttribute("user", user);
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "admin";
    }
    @GetMapping(value = "/admin/users")
    public String printUsers(ModelMap model) {
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "admin";
    }
    @GetMapping(value = "/admin/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        return "new";
    }
    @PostMapping(value = "/admin/users")
    public String saveNewUser(@ModelAttribute("user") User user) {
        userServiceImpl.save(user);
        return "redirect:/admin";
    }
    @GetMapping(value = "/admin/{id}/edit")
    public String edit(ModelMap model, @PathVariable("id") int id) {
        model.addAttribute("user", userServiceImpl.show(id));
        return "edit";
    }
    @PostMapping(value = "/admin/users/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userServiceImpl.update(id, user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        userServiceImpl.delete(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete")
    public String isExistById(@PathVariable User user) {
        userServiceImpl.delete(Math.toIntExact(user.getId()));
        return "redirect:/admin/users";
    }
}