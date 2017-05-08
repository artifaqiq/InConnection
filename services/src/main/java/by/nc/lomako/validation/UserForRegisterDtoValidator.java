/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.validation;

import by.nc.lomako.dto.UserForRegisterDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
public class UserForRegisterDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserForRegisterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForRegisterDto user = (UserForRegisterDto) target;

        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            errors.rejectValue("passwordConfirmation", "Incorrect password confirmation");
        }

        Pattern emailPattern = Pattern
                .compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher emailMatcher = emailPattern.matcher(user.getEmail());
        if (!emailMatcher.matches()) {
            errors.rejectValue("email", "Invalid email format");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "empty firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "empty lastName");

    }
}
