package ru.kata.spring.boot_security.demo.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;



@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "role", unique = true)
    private String role;

    public Role() {
    }
    public Role(String role) {
       this.role = role;
    }


    public Integer getId() {
        return id;
    }


    public String getRole() {
        return role;
    }


    @Override
    public @Nullable String getAuthority() {
        return role;
    }
}
