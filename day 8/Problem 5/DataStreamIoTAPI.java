import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

class TelemetryDTO {

    @NotBlank(message = "Sensor ID is required")
    private String sensorId;

    private Double temperature;

    public String getSensorId() {
        return sensorId;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}

@RestController
@RequestMapping("/api/v1")
class TelemetryController {

    @PostMapping("/telemetry")
    public ResponseEntity<String> ingestTelemetry(
            @RequestBody TelemetryDTO telemetryDTO) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Telemetry received successfully");
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
            HttpMessageNotReadableException.class
    )
    public ResponseEntity<Map<String, String>>
    handleMalformedJson(
            HttpMessageNotReadableException ex) {

        Map<String, String> error =
                new HashMap<>();

        String message =
                ex.getMostSpecificCause() != null
                        ? ex.getMostSpecificCause().getMessage()
                        : ex.getMessage();

        error.put(
                "error",
                "Invalid JSON payload"
        );

        error.put(
                "details",
                message
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}

public class DataStreamIoTAPI {

    public static void main(String[] args) {

        System.out.println(
                "DataStream IoT API");
    }
}