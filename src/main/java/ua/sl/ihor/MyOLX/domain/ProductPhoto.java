package ua.sl.ihor.MyOLX.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_photo")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ProductPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public ProductPhoto(Product product, String photoFileName) {
        this.product = product;
        this.photoFileName = photoFileName;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @Column(name = "photo_file_name", unique = true)
    private String photoFileName;
}
