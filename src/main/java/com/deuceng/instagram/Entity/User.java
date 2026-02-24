package com.deuceng.instagram.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Spring'in bu sınıfı izlemesini sağlar
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private int id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String bio;
    @Column
    private String profile_url;
    @Column
    private boolean isPrivate;
    @Column(updatable=false)
    @CreatedDate
    private LocalDate created_at;

    @OneToMany(mappedBy = "following",cascade = CascadeType.ALL)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "follower",cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();
}
