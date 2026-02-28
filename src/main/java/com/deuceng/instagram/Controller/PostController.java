package com.deuceng.instagram.Controller;

import com.deuceng.instagram.DTO.PostRequestDto;
import com.deuceng.instagram.Entity.Post;
import com.deuceng.instagram.Service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/upload")
    public ResponseEntity<Post> createPost(@RequestParam("file")MultipartFile file, @RequestParam("caption") String caption) throws IOException {
        Post savedPost = postService.createPost(file,caption);
        return ResponseEntity.ok(savedPost);
    }
}
