package com.orderingsystem.orderingsystem.dto.request;

import com.orderingsystem.orderingsystem.entity.Role;
import com.orderingsystem.orderingsystem.entity.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsersRequest {

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

    @NotNull(message = "Point cannot be empty")
    @Min(value = 0, message = "Point cannot be lower than 0")
    private Integer point;

    private Status status;

    private Role role;
}
