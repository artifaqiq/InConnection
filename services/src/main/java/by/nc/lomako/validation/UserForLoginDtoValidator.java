/**
 * Copyright (c) 2017, Lomako. All rights reserved.
 */
package by.nc.lomako.validation;

import by.nc.lomako.dto.UserForLoginDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Lomako
 * @version 1.0
 */
@Component
public class UserForLoginDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserForLoginDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
