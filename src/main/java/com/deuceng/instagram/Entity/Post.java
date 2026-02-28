package com.deuceng.instagram.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column
    private String caption;
    @Column
    private String image_url;
    // HATA BURADAN KAYNAKLANIYOR: Bu alanın ismi tam olarak 'createdAt' olmalı
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy = "post")
    List<PostLike> likers = new ArrayList<>();

    // Uygulama her post oluşturduğunda tarihi otomatik set etsin diye:
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
