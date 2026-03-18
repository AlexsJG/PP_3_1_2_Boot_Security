package ru.kata.spring.boot_security.demo.usersForTables;

import jakarta.annotation.PostConstruct;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FillingOutUsers {

    private final UserService userServiceImpl;

    private final RoleService roleServiceImpl;

    public FillingOutUsers(UserService userServiceImpl, RoleService roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }


    @PostConstruct
    public void fillUsers() {
        User user = new User("user", "user", 20, "user@mail.ru", "user");
        User admin = new User("admin", "admin", 30, "admin@mail.ru", "admin");
        Role role1 = new Role("ROLE_ADMIN");
        Role role2 = new Role("ROLE_USER");
        roleServiceImpl.save(role1);
        roleServiceImpl.save(role2);
        admin.setRoles(new ArrayList<>(Arrays.asList(role1, role2)));
        user.setRoles(new ArrayList<>(List.of(role2)));
        userServiceImpl.save(admin);
        userServiceImpl.save(user);
    }

    @PreDestroy
    public void dropTable() {
        userServiceImpl.dropTable();
        roleServiceImpl.dropTable();
    }
}
