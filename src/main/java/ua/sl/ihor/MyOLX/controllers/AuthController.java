package ua.sl.ihor.MyOLX.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.sl.ihor.MyOLX.DTO.SignInDTO;
import ua.sl.ihor.MyOLX.DTO.UserDTO;
import ua.sl.ihor.MyOLX.domain.User;
import ua.sl.ihor.MyOLX.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final TokenBasedRememberMeServices rememberMeServices;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @PostMapping
    public UserDTO auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SignInDTO signInDTO = objectMapper.readValue(request.getInputStream(), SignInDTO.class);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword()));

        if (signInDTO.isRememberMe()) {
            rememberMeServices.onLoginSuccess(request, response, authentication);
        } else {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        }

        log.info("User with id: {}, successfully signed in.", ((User) authentication.getPrincipal()).getId());
        return new UserDTO((User) authentication.getPrincipal());
    }

    @GetMapping("/me")
    private UserDTO authMe(@AuthenticationPrincipal User user) {
        log.info("In authMe user with id: {}.", user.getId());
        return new UserDTO(userService.findById(user.getId()));
    }
}
