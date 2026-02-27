package com.deuceng.instagram.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        // Çevresel değişkenlerden verileri çekiyoruz
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", System.getenv("CLOUDINARY_NAME"),
                "api_key", System.getenv("CLOUDINARY_KEY"),
                "api_secret", System.getenv("CLOUDINARY_SECRET")
        ));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        // Cloudinary'ye yükleme yapıyoruz
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        // Bize dönen güvenli URL'yi (https) geri döndürüyoruz
        return uploadResult.get("secure_url").toString();
    }
}