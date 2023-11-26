package com.wasd.website.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;
    
    @Column(name = "password", nullable = false, length = 500)
    private String password;
    
    @Column(name = "enabled", nullable = false)
    private boolean isEnabled;
    
    @OneToMany(mappedBy = "authority")
    private Set<Authority> authorities;
}
