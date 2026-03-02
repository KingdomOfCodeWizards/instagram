package com.deuceng.instagram.service.impl;

import com.deuceng.instagram.dto.PostResponseDto;
import com.deuceng.instagram.dto.UserSummaryDto;
import com.deuceng.instagram.entity.Post;
import com.deuceng.instagram.entity.User;
import com.deuceng.instagram.repository.PostRepository;
import com.deuceng.instagram.repository.UserRepository;
import com.deuceng.instagram.service.ICloudinaryService;
import com.deuceng.instagram.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ICloudinaryService cloudinaryService;

    @Override
    public PostResponseDto createPost(MultipartFile file, String caption) {

        // 1) JWT'den kullanıcı adını al
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Yetkisiz erişim");
        }
        String username = auth.getName();

        // 2) Kullanıcıyı DB'den bul
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 3) Cloudinary'ye yükle
        String imageUrl = cloudinaryService.uploadImage(file);

        // 4) Post oluştur
        Post post = new Post();
        post.setCaption(caption);
        post.setImage_url(imageUrl); // sende field bu isimdeyse
        post.setUser(user);

        // 5) DB'ye kaydet
        Post savedPost = postRepository.save(post);

        // 6) DTO olarak dön
        return toPostResponse(savedPost);
    }

    private PostResponseDto toPostResponse(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .imageUrl(post.getImage_url()) // sende getImageUrl() ise düzelt
                .createdAt(post.getCreatedAt())
                .user(UserSummaryDto.builder()
                        .id((long) post.getUser().getId()) // User.id int ise cast; Long ise direkt ver
                        .username(post.getUser().getUsername())
                        .profileUrl(post.getUser().getProfile_url()) // sende getProfileUrl() ise düzelt
                        .build())
                .build();
    }
}