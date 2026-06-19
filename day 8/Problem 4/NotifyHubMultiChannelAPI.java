import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

interface SmsGroup {
}

interface EmailGroup {
}

class AlertConfigDTO {

    @NotBlank(
            message = "Phone number is required",
            groups = SmsGroup.class
    )
    private String phoneNumber;

    @NotBlank(
            message = "Email address is required",
            groups = EmailGroup.class
    )
    @Email(
            message = "Invalid email format",
            groups = EmailGroup.class
    )
    private String emailAddress;

    private String message;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getMessage() {
        return message;
    }
}

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController {

    @PostMapping("/sms")
    public ResponseEntity<String> sendSms(

            @RequestBody
            @Validated(SmsGroup.class)
            AlertConfigDTO request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("SMS notification configured");
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(

            @RequestBody
            @Validated(EmailGroup.class)
            AlertConfigDTO request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Email notification configured");
    }
}

public class NotifyHubMultiChannelAPI {

    public static void main(String[] args) {

        System.out.println(
                "NotifyHub Multi Channel API");
    }
}