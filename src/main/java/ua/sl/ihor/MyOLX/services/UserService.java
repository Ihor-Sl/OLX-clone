package ua.sl.ihor.MyOLX.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Validator;
import ua.sl.ihor.MyOLX.domain.User;

public interface UserService extends UserDetailsService, Validator {
    User findById(long id);

    User update(long id, User updatedUser);

    void delete(long id);

    void register(User newUser);

    void setOnlineAndLastVisit(Long userId, boolean isOnline);
}
