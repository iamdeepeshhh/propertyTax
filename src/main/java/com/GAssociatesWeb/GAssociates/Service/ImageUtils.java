package com.GAssociatesWeb.GAssociates.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class ImageUtils {
    public static String convertToBase64(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }
            byte[] bytes = file.getBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            System.err.println("Error converting file to Base64: " + e.getMessage());
            return null;
        }
    }


}
