package com.myshop.api.util;

import com.myshop.api.dto.User;
import com.myshop.api.exception.EmptyPasswordException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordEncryptor {

    public static void bcrypt(User user) {
        user.setPassword(bcrypt(user.getPassword()));
    }

    public static String bcrypt(String password) {
        checkPassword(password);
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static void checkPassword(String password) {
        if(password.isBlank()) throw new EmptyPasswordException("패스워드가 비어있습니다.");
    }

    public static boolean isMatchBcrypt(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
