package ua.sl.ihor.MyOLX.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import ua.sl.ihor.MyOLX.domain.Role;
import ua.sl.ihor.MyOLX.domain.User;
import ua.sl.ihor.MyOLX.exceptions.NotFoundException;
import ua.sl.ihor.MyOLX.exceptions.PropertyAlreadyTakenException;
import ua.sl.ihor.MyOLX.repositories.UserRepository;
import ua.sl.ihor.MyOLX.services.UserService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(long id) {
        log.info("Finding user by id: {}.", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void register(User newUser) {
        newUser.setRoles(Set.of(Role.ROLE_USER));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setAccountNonLocked(true);
        log.info("Registered new user: {}.", newUser);
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public User update(long id, User updatedUser) {
        log.info("Updating user with id: {}.", id);
        User currentUser = findById(id);
        if (!canPlayerSetThisName(currentUser, updatedUser.getName())) {
            log.info("Name: {} is taken.", updatedUser.getName());
            throw new PropertyAlreadyTakenException("This name is already taken");
        }
        if (!canPlayerSetThisEmail(currentUser, updatedUser.getEmail())) {
            log.info("Email: {} is taken.", updatedUser.getEmail());
            throw new PropertyAlreadyTakenException("This email is already taken");
        }
        currentUser.setName(updatedUser.getName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setLocation(updatedUser.getLocation());
        log.info("Updating success.");
        return userRepository.save(currentUser);
    }

    private boolean canPlayerSetThisName(User user, String name) {
        if (user.getName().equals(name)) return true;
        return !userRepository.existsByName(name);
    }

    private boolean canPlayerSetThisEmail(User user, String email) {
        if (user.getEmail().equals(email)) return true;
        return !userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void delete(long id) {
        User userToDelete = findById(id);
        userToDelete.setDeleted(true);
        userRepository.save(userToDelete);
        log.info("Deleted user: {}.", userToDelete);
    }

    @Override
    @Transactional
    public void setOnlineAndLastVisit(Long userId, boolean isOnline) {
        userRepository.setOnlineAndLastVisit(userId, isOnline);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {} (loadUserByUsername).", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        log.info("Validating user: {}.", user);
        if (userRepository.existsByEmail(user.getEmail())) {
            log.info("Email is taken: {}.", user.getEmail());
            errors.rejectValue("email", "", "This email is already taken");
        }
        if (userRepository.existsByName(user.getName())) {
            log.info("Name is taken: {}.", user.getName());
            errors.rejectValue("name", "", "This name is already taken");
        }
    }
}
