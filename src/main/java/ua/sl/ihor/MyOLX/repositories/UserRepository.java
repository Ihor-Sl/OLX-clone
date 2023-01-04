package ua.sl.ihor.MyOLX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.sl.ihor.MyOLX.domain.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query(value = "update usr set last_visit = now(), is_online = :isOnline WHERE id = :id", nativeQuery = true)
    void setOnlineAndLastVisit(long id, boolean isOnline);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
