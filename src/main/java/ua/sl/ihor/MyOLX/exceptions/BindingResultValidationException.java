package ua.sl.ihor.MyOLX.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class BindingResultValidationException extends RuntimeException {
    public BindingResultValidationException(BindingResult bindingResult) {
        super(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" ")));
    }
}
