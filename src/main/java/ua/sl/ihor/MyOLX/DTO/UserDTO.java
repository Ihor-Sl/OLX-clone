package ua.sl.ihor.MyOLX.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.sl.ihor.MyOLX.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private long userId;
    private String name;
    private String email;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private LocalDateTime lastVisit;
    private List<ProductPreviewDTO> products;

    public UserDTO(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.location = user.getLocation();
        this.lastVisit = user.getLastVisit();
        this.products = user.getProducts().stream().map(ProductPreviewDTO::new).collect(Collectors.toList());
    }
}
