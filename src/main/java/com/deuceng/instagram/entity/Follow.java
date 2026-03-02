package com.deuceng.instagram.entity;

import com.deuceng.instagram.enums.FollowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(
        name="follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
public class Follow {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //fetch:follow çekilmek istendiğinde user bilgilerinin de gelmemesi için
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="follower_id")
    private User follower;

    //optional=false: follow da following de null olamaz
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name="following_id")
    private User following;

    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FollowStatus status;
}
