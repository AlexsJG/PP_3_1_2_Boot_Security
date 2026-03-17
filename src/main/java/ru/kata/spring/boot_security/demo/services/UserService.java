package ru.kata.spring.boot_security.demo.services;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();
    User findById(Integer id);
    void save(User user);
    void update(User user);
    void deleteById(Integer id);

    boolean isAdmin(User user);

    @Transactional
    void saveUserWithRoles(User user, Integer[] roleIds);

    @Transactional
    void updateUserWithRoles(Integer id, User updatedUser, Integer[] roleIds);
}
