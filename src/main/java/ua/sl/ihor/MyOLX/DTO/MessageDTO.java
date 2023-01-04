package ua.sl.ihor.MyOLX.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.sl.ihor.MyOLX.domain.Message;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private String body;
    private long userId;
    private long productId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private LocalDateTime sentAt;

    public MessageDTO(Message message) {
        this.body = message.getBody();
        this.userId = message.getUser().getId();
        this.productId = message.getProduct().getId();
        this.sentAt = message.getSentAt();
    }
}
