/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dto.user;

import by.nc.lomako.pojos.constants.StringLength;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserForUpdateDto {
    private String email;
    private String firstName;
    private String lastName;

    @Component
    public static class DtoValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return UserForUpdateDto.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            UserForUpdateDto user = (UserForUpdateDto) target;

            Pattern emailPattern = Pattern
                    .compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
            Matcher emailMatcher = emailPattern.matcher(user.getEmail());
            if (!emailMatcher.matches()) {
                errors.rejectValue("email", "email.not.valid");
            }

            if (user.getEmail().length() > StringLength.EMAIL) {
                errors.rejectValue("email", "email.too.long");
            }

            rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
            if (user.getFirstName().length() > StringLength.FIRST_NAME) {
                errors.rejectValue("firstName", "firstName.too.long");
            }

            rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");
            if (user.getLastName().length() > StringLength.LAST_NAME) {
                errors.rejectValue("lastName", "lastName.too.long");
            }
        }
    }
}
