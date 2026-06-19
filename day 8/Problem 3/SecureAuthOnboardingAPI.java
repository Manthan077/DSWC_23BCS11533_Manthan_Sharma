import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsMatchValidator.class)
@interface PasswordsMatch {

    String message()
            default "Passwords do not match";

    Class<?>[] groups()
            default {};

    Class<? extends Payload>[] payload()
            default {};
}

class PasswordsMatchValidator
        implements ConstraintValidator<
        PasswordsMatch,
        RegistrationRequestDTO> {

    @Override
    public boolean isValid(
            RegistrationRequestDTO dto,
            ConstraintValidatorContext context) {

        if (dto == null) {
            return true;
        }

        return dto.getPassword()
                .equals(dto.getConfirmPassword());
    }
}

@PasswordsMatch
class RegistrationRequestDTO {

    @NotBlank
    private String username;

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    @NotBlank
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}

class UserResponseDTO {

    private String username;
    private String email;

    public UserResponseDTO(
            String username,
            String email) {

        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO>
    registerUser(

            @Valid
            @RequestBody
            RegistrationRequestDTO request) {

        UserResponseDTO response =
                new UserResponseDTO(
                        request.getUsername(),
                        request.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<
            Map<String, String>>
    handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors =
                new HashMap<>();

        for (FieldError fieldError :
                ex.getBindingResult()
                        .getFieldErrors()) {

            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage());
        }

        for (ObjectError objectError :
                ex.getBindingResult()
                        .getGlobalErrors()) {

            errors.put(
                    objectError.getObjectName(),
                    objectError.getDefaultMessage());
        }

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}

public class SecureAuthOnboardingAPI {

    public static void main(String[] args) {

        System.out.println(
                "SecureAuth Onboarding API");
    }
}