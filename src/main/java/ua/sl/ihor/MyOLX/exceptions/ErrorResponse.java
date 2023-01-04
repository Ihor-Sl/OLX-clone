package ua.sl.ihor.MyOLX.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private boolean error = true;

    private final int status;

    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.now();

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
