import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = BugReportDTO.class,
                name = "BUG"
        ),
        @JsonSubTypes.Type(
                value = FeatureRequestDTO.class,
                name = "FEATURE"
        )
})
abstract class TicketRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(
            max = 100,
            message = "Title cannot exceed 100 characters"
    )
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

class BugReportDTO extends TicketRequestDTO {

    @Min(
            value = 1,
            message = "Severity must be at least 1"
    )
    @Max(
            value = 5,
            message = "Severity must not exceed 5"
    )
    private int severity;

    private String stepsToReproduce;

    public int getSeverity() {
        return severity;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }
}

class FeatureRequestDTO
        extends TicketRequestDTO {

    @Positive(
            message =
                    "Business value points must be greater than 0"
    )
    private int businessValuePoints;

    private String targetAudience;

    public int getBusinessValuePoints() {
        return businessValuePoints;
    }

    public String getTargetAudience() {
        return targetAudience;
    }
}

class UnsupportedTicketTypeException
        extends RuntimeException {

    public UnsupportedTicketTypeException(
            String message) {

        super(message);
    }
}

@RestController
@RequestMapping("/api/v1/tickets")
class TicketController {

    @PostMapping
    public ResponseEntity<String> createTicket(
            @Valid
            @RequestBody
            TicketRequestDTO request) {

        if (request.getClass()
                .getSimpleName()
                .equals("HotfixDTO")) {

            throw new UnsupportedTicketTypeException(
                    "HOTFIX ticket type is deprecated");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Ticket Created Successfully");
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, String>>
    handleValidationException(
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

    @ExceptionHandler(
            UnsupportedTicketTypeException.class
    )
    public ResponseEntity<String>
    handleUnsupportedTicketType(
            UnsupportedTicketTypeException ex) {

        return ResponseEntity
                .status(
                        HttpStatus.UNPROCESSABLE_ENTITY
                )
                .body(ex.getMessage());
    }
}

public class DevTrackIssueTrackerAPI {

    public static void main(String[] args) {

        System.out.println(
                "DevTrack Issue Tracker API"
        );
    }
}