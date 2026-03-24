package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("currentUser", currentUser);
        return "admin";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("newUser") User user) {
        userService.saveUserWithRoles(user);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Integer id,
                             @ModelAttribute User user) {

        userService.updateUserWithRoles(id, user);
        return "redirect:/admin";
    }


    @GetMapping("/users/{id}/details")
    @ResponseBody
    public User getUserDetails(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
