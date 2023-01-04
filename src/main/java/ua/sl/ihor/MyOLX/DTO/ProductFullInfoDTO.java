package ua.sl.ihor.MyOLX.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.sl.ihor.MyOLX.domain.Product;
import ua.sl.ihor.MyOLX.domain.ProductPhoto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductFullInfoDTO {
    private long id;
    private String title;
    private String description;
    private long price;
    private String location;
    private String userName;
    private long userId;
    private String previewPhotoFileName;
    private List<String> photosFileNames;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDateTime addedAt;

    public ProductFullInfoDTO(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.location = product.getLocation();
        this.userName = product.getUser().getName();
        this.userId = product.getUser().getId();
        this.previewPhotoFileName = product.getPreviewPhoto();
        this.photosFileNames = product.getProductPhotos().stream().map(ProductPhoto::getPhotoFileName).collect(Collectors.toList());
        this.addedAt = product.getAddedAt();
    }
}
