package com.deuceng.instagram.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long  id;

    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id",nullable=false)
    private Post post;

    @Column(nullable=false,length=1000)
    private String content;
}
