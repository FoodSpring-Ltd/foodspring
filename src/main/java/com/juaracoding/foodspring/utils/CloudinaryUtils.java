package com.juaracoding.foodspring.utils;
/*
IntelliJ IDEA 2022.2.2 (Community Edition)
Build #IC-222.4167.29, built on September 13, 2022
Runtime version: 17.0.4+7-b469.53 amd64
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 8/20/2023 10:37 PM
@Last Modified 8/20/2023 10:37 PM
Version 1.0
*/

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Component
public class CloudinaryUtils {

    @Autowired
    private Cloudinary cloudinary;

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public Map<String, String> uploadFileToCloudinary(MultipartFile file, String publicId) throws IOException {

        Map params = ObjectUtils.asMap(
                "public_id", String.format("%s", publicId),
                "folder", "product_images",
                "overwrite", true,
                "resource_type", "image"
        );
        Map<String, String> resultMap = cloudinary.uploader().upload(file.getBytes(), params);
        return resultMap; // Returns the URL of the uploaded image

    }
}
