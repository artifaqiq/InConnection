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

    public static String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public static boolean checkPassword(String password, String encryptedPassword) {
        return new BCryptPasswordEncoder().matches(password, encryptedPassword);

    }
}
