package com.orderingsystem.orderingsystem.dto.request;

import com.orderingsystem.orderingsystem.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.orderingsystem.orderingsystem.dto.request.SignUpRequest.PasswordMatches; // import chính nó

@Data
@PasswordMatches
public class SignUpRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 30, message = "Username must be 4-30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username contains invalid characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "Password must contain uppercase, lowercase, number, and special character"
    )
    private String password;

    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmPassword;

    private Role role;

    @jakarta.validation.Constraint(validatedBy = PasswordMatchesValidator.class)
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface PasswordMatches {
        String message() default "Passwords do not match";
        Class<?>[] groups() default {};
        Class<? extends jakarta.validation.Payload>[] payload() default {};
    }

    public static class PasswordMatchesValidator implements jakarta.validation.ConstraintValidator<PasswordMatches, SignUpRequest> {
        @Override
        public boolean isValid(SignUpRequest request, jakarta.validation.ConstraintValidatorContext context) {
            if (request.getPassword() == null || request.getConfirmPassword() == null) {
                return false;
            }
            return request.getPassword().equals(request.getConfirmPassword());
        }
    }
}
