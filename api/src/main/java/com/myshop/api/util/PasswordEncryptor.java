package com.myshop.api.util;

import com.myshop.api.domain.dto.request.UserDto;
import com.myshop.api.exception.EmptyPasswordException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordEncryptor {

    public static void bcrypt(UserDto user) {
        user.setPassword(bcrypt(user.getPassword()));
    }

    public static String bcrypt(String password) {
        checkPassword(password);
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static void checkPassword(String password) {
        if(password.isBlank()) throw new EmptyPasswordException();
    }

    public static boolean isMatchBcrypt(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
