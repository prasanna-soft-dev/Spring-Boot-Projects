package com.password.demo.Service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class PasswordGenratorService {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?/";


    public String generatePassword(int length){
        if(length < 8 || length > 16){
            throw new IllegalArgumentException("Password length must greater than 8 and less than 16");
        }

        String characterPool = UPPERCASE + LOWERCASE + NUMBERS + SPECIAL_CHARACTERS;

        Random random = new Random();

        StringBuilder pass = new StringBuilder();

        pass.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        pass.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        pass.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        pass.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));


        for(int i = 4;i < length;i++){
            pass.append(characterPool.charAt(random.nextInt(characterPool.length())));
        }

        return pass.toString();
        
    }

}
