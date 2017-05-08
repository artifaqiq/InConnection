/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Lomako
 * @version 1.0
 */
public class PasswordCryptographyUtil {
    private static final String SALT = "*Yh0*YhUT^&YGjj";

    public static String encryptPassword(String email, String password) {

        StringBuilder toEncrypt = new StringBuilder()
                .append(email)
                .append(password)
                .append(SALT);

        return new BCryptPasswordEncoder().encode(password);
    }

    public static boolean checkPassword(String email, String password, String encryptedPassword) {
        StringBuilder toEncrypt = new StringBuilder()
                .append(email)
                .append(password)
                .append(SALT);

        System.out.println(password);
        System.out.println(new BCryptPasswordEncoder().encode(password));
        System.out.println(encryptedPassword);

        return new BCryptPasswordEncoder().matches(password, encryptedPassword);

    }
}
