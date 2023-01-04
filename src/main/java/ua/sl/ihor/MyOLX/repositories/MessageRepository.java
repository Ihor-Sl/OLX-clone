package ua.sl.ihor.MyOLX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.sl.ihor.MyOLX.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    boolean existsByProductId(long productId);

    @Query(value = "select user_id from message where not user_id = :userId and product_id = :productId", nativeQuery = true)
    long findDialogMemberId(long userId, long productId);
}
