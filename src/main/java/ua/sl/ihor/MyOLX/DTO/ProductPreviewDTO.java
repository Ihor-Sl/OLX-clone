package ua.sl.ihor.MyOLX.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.sl.ihor.MyOLX.domain.Product;

import java.time.LocalDateTime;

@Data
public class ProductPreviewDTO {
    private long id;
    private String previewPhotoFileName;
    private String title;
    private String description;
    private long price;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime addedAt;

    public ProductPreviewDTO(Product product) {
        this.id = product.getId();
        this.previewPhotoFileName = product.getPreviewPhoto();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.location = product.getLocation();
        this.addedAt = product.getAddedAt();
    }
}
