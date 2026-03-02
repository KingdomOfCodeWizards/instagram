package com.deuceng.instagram.service;

import com.deuceng.instagram.dto.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface IPostService {
    PostResponseDto createPost(MultipartFile file, String caption);
}
