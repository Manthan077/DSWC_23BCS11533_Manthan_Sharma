import jakarta.validation.Valid;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SupportedCurrencyValidator.class)
@interface SupportedCurrency {

    String message()
            default "Unsupported currency";

    Class<?>[] groups()
            default {};

    Class<? extends Payload>[] payload()
            default {};
}

class SupportedCurrencyValidator
        implements ConstraintValidator<
        SupportedCurrency,
        String> {

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        return value.equals("USD")
                || value.equals("EUR")
                || value.equals("GBP");
    }
}

class TransactionDTO {

    @Positive(
            message =
                    "Amount must be greater than 0"
    )
    private BigDecimal amount;

    @SupportedCurrency
    private String currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}

class BatchRequestDTO {

    @NotBlank(
            message =
                    "Batch ID cannot be blank"
    )
    private String batchId;

    @Valid
    private List<TransactionDTO> transactions;

    public String getBatchId() {
        return batchId;
    }

    public List<TransactionDTO>
    getTransactions() {

        return transactions;
    }
}

@RestController
@RequestMapping("/api/v1/batches")
class BatchController {

    @PostMapping
    public ResponseEntity<String> createBatch(

            @Valid
            @RequestBody
            BatchRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(
                        "Batch Accepted Successfully"
                );
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

        for (FieldError error :
                ex.getBindingResult()
                        .getFieldErrors()) {

            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}

public class LedgerXBulkTransactionAPI {

    public static void main(String[] args) {

        System.out.println(
                "LedgerX Bulk Transaction API");
    }
}