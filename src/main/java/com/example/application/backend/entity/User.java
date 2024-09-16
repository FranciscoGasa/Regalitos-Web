package com.example.application.backend.entity;

import com.example.application.backend.Role;
import com.example.application.backend.entity.Friend;
import com.example.application.backend.service.RegalitosService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.UUID;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Entity
public class User extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String firstname= "";

    private String lastname= "";

    @JsonIgnore
    private String hashedPassword;

    @NotNull
    @NotEmpty
    private String username= "";

    @Email
    @NotNull
    @NotEmpty
    private String email= "";

    private Role role;

    private String confirmationToken;

    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new LinkedList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiftList> giftlist = new LinkedList<>();


    public List<GiftList> getGiftList() {
        return giftlist;
    }
    public void setGiftList(List<GiftList> giftlist) {
        this.giftlist = giftlist;
    }

    public List<Friend> getFriends() {
        return friends;
    }
    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public void addFriend(Friend friend) {
        if (friend != null) {
            this.friends.add(friend);
            friend.setUser(this);
        }
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }
    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
    public boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
