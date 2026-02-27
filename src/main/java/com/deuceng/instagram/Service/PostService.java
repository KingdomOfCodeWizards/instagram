package com.deuceng.instagram.Service;

import com.deuceng.instagram.Entity.Post;
import com.deuceng.instagram.Entity.User;
import com.deuceng.instagram.Repository.PostRepository;
import com.deuceng.instagram.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor

public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;

    public Post createPost(MultipartFile file, String  caption)throws IOException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String image_url = cloudinaryService.uploadImage(file);
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        // 4. Post nesnesini oluştur ve kaydet
        Post post = new Post();
        post.setImage_url(image_url);
        post.setCaption(caption);
        post.setUser(user);
        return postRepository.save(post);
    }
}
