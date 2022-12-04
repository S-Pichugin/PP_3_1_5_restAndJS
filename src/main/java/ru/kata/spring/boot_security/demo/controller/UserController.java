package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserSecurityService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserSecurityService userSecurityService;
    @Autowired
    public void setUserSecurityService(UserSecurityService userSecurityService){
        this.userSecurityService = userSecurityService;
    }

    @GetMapping(value = "/user")
    public String userPage (Principal principal, ModelMap model) {
        User user = userSecurityService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

//    @GetMapping
//    public String getAllUsers(@ModelAttribute("user") User user, Model model, Authentication authentication) {
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("principal", authentication);
//        model.addAttribute("rolesList", user.getRoles());
//        model.addAttribute("admin", userSecurityService.findByUsername(authentication.name()));
//        return "admin/admin";
//    }

    @GetMapping(value = "/admin")
    public String print(Principal principal,ModelMap model) {
        User user = userSecurityService.findByUsername(principal.getName());
        User userEmpty = new User();
        model.addAttribute("userEmpty", userEmpty);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

//    @GetMapping(value = "/admin")
//    public String printLoginUser(Principal principal,ModelMap model) {
//        User user = userSecurityService.findByUsername(principal.getName());
//        model.addAttribute("user", user);
//        return "users";
//    }

    @GetMapping(value = "/admin/users")
    public String printUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping(value = "/admin/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping(value = "/admin/users")
    public String saveNewUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/{id}/edit")
    public String edit(ModelMap model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.show(id));
        return "edit";
    }

    @PostMapping(value = "/admin/users/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.update(id, user);
        return "redirect:/admin";
    }


    @PostMapping(value = "/admin/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete")
    public String isExistById(@PathVariable User user) {
        userService.delete(Math.toIntExact(user.getId()));
        return "redirect:/admin/users";
    }
}