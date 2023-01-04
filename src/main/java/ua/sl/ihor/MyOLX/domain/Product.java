package ua.sl.ihor.MyOLX.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Where(clause = "is_deleted = false")
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"title"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Size(min = 3, max = 100, message = "Min name length - 3, max title length - 100")
    @NotNull(message = "Title required!")
    private String title;

    @Column(name = "description", columnDefinition = "varchar(2000)")
    @Size(max = 2000, message = "Max description length - 2000")
    private String description;

    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Min price: 1")
    @Max(value = 1_000_000_000, message = "Max price: 1_000_000_000")
    @NotNull(message = "Please set price")
    private long price;

    @Column(name = "preview_photo_file_name", nullable = false, unique = true)
    private String previewPhoto;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductPhoto> productPhotos;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @OneToMany
    private List<Message> messages;

    @PrePersist
    private void onCreate() {
        addedAt = LocalDateTime.now();
    }

    @Column(name = "is_deleted", columnDefinition = "boolean not null default false")
    private boolean isDeleted;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
