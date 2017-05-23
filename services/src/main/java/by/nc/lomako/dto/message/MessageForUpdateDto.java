/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.dto.message;

import by.nc.lomako.pojos.constants.StringLength;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

/**
 * @author Lomako
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageForUpdateDto {

    private String body;
    private boolean isRead;
    private long userFromId;
    private long userToId;

    @Component
    public static class DtoValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return MessageForUpdateDto.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {

            MessageForUpdateDto messageDto = (MessageForUpdateDto) target;

            rejectIfEmptyOrWhitespace(errors, "body", "body.required");
            if (messageDto.getBody().length() > StringLength.TEXT) {
                errors.rejectValue("body", "body.too.long");
            }
        }
    }
}
