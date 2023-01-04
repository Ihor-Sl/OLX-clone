package ua.sl.ihor.MyOLX.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.sl.ihor.MyOLX.exceptions.ErrorResponse;
import ua.sl.ihor.MyOLX.services.impl.UserServiceImpl;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserServiceImpl userService;
    private final ObjectMapper mapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/root").permitAll();
                    auth.requestMatchers("/api/v1/images/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll();
                    auth.requestMatchers("/api/v1/products/id/{id}").permitAll();
                    auth.requestMatchers("/api/v1/auth").permitAll();
                    auth.requestMatchers("/api/v1/users/registration").permitAll();
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(exh ->
                        exh.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            mapper.writeValue(response.getOutputStream(), new ErrorResponse("Please sign in!", HttpStatus.UNAUTHORIZED));
                        })
                )
                .formLogin().disable()
                .rememberMe(Customizer.withDefaults())
                .logout(l -> {
                    l.logoutUrl("/api/v1/logout");
                    l.logoutSuccessHandler((request, response, authentication) -> response.setStatus(200));
                })
                .addFilterBefore(
                        new RememberMeAuthenticationFilter(authenticationManager(http), tokenBasedRememberMeServices()),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Value("${images-storage-path}")
            private String imagesStoragePath;

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/api/v1/images/**")
                        .addResourceLocations("file:///" + imagesStoragePath + "/");
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:3000")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/"));
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(rememberMeAuthenticationProvider());
        return auth.build();
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("key");
    }

    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        return new TokenBasedRememberMeServices("key", userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
