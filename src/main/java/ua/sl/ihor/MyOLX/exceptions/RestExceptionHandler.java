package ua.sl.ihor.MyOLX.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final TokenBasedRememberMeServices tokenBasedRememberMeServices;

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse BadCredentialsException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        request.getSession().invalidate();
        tokenBasedRememberMeServices.loginFail(request, response);
        return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            BindingResultValidationException.class,
            NotFoundException.class,
            PropertyAlreadyTakenException.class,
            IncorrectFileFormat.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse errorResponse(Exception ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
