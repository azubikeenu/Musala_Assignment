package com.azubike.ellipsis.droneapplication.medications.web.mappers;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Random;

@Component
public class GenerateUniqueCode {

    public String generateRandomCode(String code) {
        return !StringUtils.hasText(code) ? generateRandomString() : code;
    }

    private String generateRandomString() {
        Random RANDOM = new Random();
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
        StringBuilder output = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            output.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return "MED_".concat(output.toString());
    }
}
