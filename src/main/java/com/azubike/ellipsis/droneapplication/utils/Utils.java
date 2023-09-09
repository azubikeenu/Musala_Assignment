package com.azubike.ellipsis.droneapplication.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Random;

@Component
public class Utils {


    public static String randomizeName(MultipartFile file){
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
         return  String.join("-", generateRandomString(10 ), fileName);
    }

    public static String generateRandomString(int length ) {
        Random RANDOM = new Random();
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder output = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            output.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return output.toString();
    }

}
