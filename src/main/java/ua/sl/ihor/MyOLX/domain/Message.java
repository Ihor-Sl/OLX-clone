package ua.sl.ihor.MyOLX.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Message {

    public Message(String body, Product product, User user) {
        this.body = body;
        this.product = product;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "body")
    @Size(max = 2000, message = "Max message length - 2000")
    @NotBlank(message = "Message text can't be empty")
    @NotNull(message = "Message text can't be empty")
    private String body;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    private void onCreate() {
        sentAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Where(clause = "is_deleted = false")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
