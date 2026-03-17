package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("users", userServiceImpl.allUsers());
        model.addAttribute("roles", roleServiceImpl.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("currentUser", currentUser);
        return "admin";
    }

    @GetMapping("/new")
    public String newUserForm(@AuthenticationPrincipal User currentUser, Model model) {
        List<Role> roles = roleServiceImpl.findAll();
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roles);
        model.addAttribute("currentUser", currentUser);
        return "adduser";
    }

    @GetMapping("/users/{id}/edit")
    public String editUserForm(@AuthenticationPrincipal User currentUser, @PathVariable Integer id, Model model) {
        model.addAttribute("user", userServiceImpl.findById(id));
        model.addAttribute("roles", roleServiceImpl.findAll());
        model.addAttribute("currentUser", currentUser);
        return "update";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam(value = "roles", required = false) Integer[] rolesIds) {
        userServiceImpl.saveUserWithRoles(user, rolesIds);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Integer id,
                             @ModelAttribute User user,
                             @RequestParam(value = "roles", required = false) Integer[] rolesIds) {

        userServiceImpl.updateUserWithRoles(id, user, rolesIds);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Integer id) {
        userServiceImpl.deleteById(id);
        return "redirect:/admin";
    }
}
