package com.deuceng.instagram.controller;

import com.deuceng.instagram.dto.PostResponseDto;
import com.deuceng.instagram.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @PostMapping("/upload")
    public ResponseEntity<PostResponseDto> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("caption") String caption
    ) {
        return ResponseEntity.ok(postService.createPost(file, caption));
    }
}
