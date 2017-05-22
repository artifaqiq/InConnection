/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dao.builders;

import by.nc.lomako.pojos.User;

/**
 * @author Lomako
 * @version 1.0
 */
public class UserBuilder {
    private String email = "test_user@example.com";
    private String firstName = "T_FIRST_NAME";
    private String lastName = "T_LAST_NAME";
    private String encryptedPassword = "T_ENC_PASSWORD";

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder encryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
        return this;
    }

    public User build() {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEncryptedPassword(encryptedPassword);
        return user;
    }

}