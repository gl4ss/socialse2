package org.example.socialse2.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.socialse2.dto.RegistrationDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationDto> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrationDto registrationDto, ConstraintValidatorContext context) {
        boolean isValid = registrationDto.getPassword().equals(registrationDto.getConfirmPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                   .addPropertyNode("password")
                   .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                   .addPropertyNode("confirmPassword")
                   .addConstraintViolation();
        }
        return isValid;
    }

}
