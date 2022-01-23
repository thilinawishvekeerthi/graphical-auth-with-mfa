package com.graphicauth.authservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user",uniqueConstraints = {@UniqueConstraint(columnNames = "userName"), @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String userName;

    @NotBlank()
    private String passWord;

    @Email
    private String email;

    private boolean active;

    private boolean mfa;

    @NotBlank()
    private String passPoints;

    @NotNull()
    private Long numberOfPassPoints;


    private String totpSecret;

    @ManyToMany(cascade = {CascadeType.PERSIST , CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name="users_roles", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();

    public void addRole(Role role){
        role.getUsers().add(this);
        this.roles.add(role);
    }
}
