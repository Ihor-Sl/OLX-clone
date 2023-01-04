package ua.sl.ihor.MyOLX.controllers.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import ua.sl.ihor.MyOLX.DTO.MessageDTO;
import ua.sl.ihor.MyOLX.DTO.UserMessageDTO;
import ua.sl.ihor.MyOLX.domain.Message;
import ua.sl.ihor.MyOLX.domain.Product;
import ua.sl.ihor.MyOLX.domain.User;
import ua.sl.ihor.MyOLX.exceptions.ErrorResponse;
import ua.sl.ihor.MyOLX.exceptions.MessageException;
import ua.sl.ihor.MyOLX.repositories.MessageRepository;
import ua.sl.ihor.MyOLX.services.ProductService;
import ua.sl.ihor.MyOLX.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocket extends AbstractWebSocketHandler {

    private final UserService userService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws IOException {
        log.info("In websocket handleTextMessage method.");
        try {
            UserMessageDTO userMessageDTO = objectMapper.readValue(msg.getPayload(), UserMessageDTO.class);

            User user = extractUserFromSession(session);
            Product product = productService.findById(userMessageDTO.getProductId());
            Message message = new Message(userMessageDTO.getBody(), product, user);

            boolean isOwnerSendsMessage = Objects.equals(user.getId(), product.getUser().getId());
            if (isOwnerSendsMessage && !messageRepository.existsByProductId(message.getProduct().getId())) {
                throw new MessageException("Product owner can't start dialog");
            }

            log.info("Saving message for product with id: {}", product.getId());
            messageRepository.save(message);

            TextMessage messageJSON = new TextMessage(objectMapper.writeValueAsString(new MessageDTO(message)));

            session.sendMessage(messageJSON);
            if (isOwnerSendsMessage) {
                this.sendMessageByUserId(messageJSON, messageRepository.findDialogMemberId(user.getId(), userMessageDTO.getProductId()));
            } else {
                this.sendMessageByUserId(messageJSON, product.getUser().getId());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST))));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) (session.getPrincipal());
        if (token != null) {
            sessions.add(session);
            User user = (User) token.getPrincipal();
            userService.setOnlineAndLastVisit(user.getId(), true);
        } else {
            session.close();
        }
    }

    private void sendMessageByUserId(TextMessage message, long userId) throws IOException {
        Optional<WebSocketSession> session = sessions.stream()
                .filter(s -> userId == extractUserFromSession(s).getId())
                .findFirst();
        if (session.isPresent())
            session.get().sendMessage(message);
    }

    private User extractUserFromSession(WebSocketSession session) {
        RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) (session.getPrincipal());
        if (token != null) {
            return (User) token.getPrincipal();
        }
        throw new RuntimeException("Can't extract user from websocket session.");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) (session.getPrincipal());
        if (token != null) {
            User user = (User) token.getPrincipal();
            userService.setOnlineAndLastVisit(user.getId(), false);
        }
    }
}
