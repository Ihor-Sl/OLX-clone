package ua.sl.ihor.MyOLX.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.sl.ihor.MyOLX.DTO.UserDTO;
import ua.sl.ihor.MyOLX.domain.User;
import ua.sl.ihor.MyOLX.exceptions.BindingResultValidationException;
import ua.sl.ihor.MyOLX.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/id/{id}")
    public UserDTO one(@PathVariable long id) {
        return new UserDTO(userService.findById(id));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult bindingResult) {
        userService.validate(user, bindingResult);
        if (bindingResult.hasErrors()) throw new BindingResultValidationException(bindingResult);
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public User update(@RequestBody @Valid User newUser, BindingResult bindingResult, @AuthenticationPrincipal User authUser) {
        if (bindingResult.hasErrors()) throw new BindingResultValidationException(bindingResult);
        return userService.update(authUser.getId(), newUser);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
