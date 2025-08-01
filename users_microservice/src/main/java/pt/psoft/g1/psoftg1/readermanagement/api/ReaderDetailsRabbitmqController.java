package pt.psoft.g1.psoftg1.readermanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.model.ReaderDetailsEvents;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderDetailsRabbitmqController {

    private final ReaderService readerService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "#{readerDetailsCreatedQueue.name}")
    public void handleReaderDetailsCreated(Message message) {
        processReaderDetailsEvent(message, ReaderDetailsEvents.READER_DETAILS_CREATED);
    }

    @RabbitListener(queues = "#{readerDetailsUpdatedQueue.name}")
    public void handleReaderDetailsUpdated(Message message) {
        processReaderDetailsEvent(message, ReaderDetailsEvents.READER_DETAILS_UPDATED);
    }

    private void processReaderDetailsEvent(Message message, String eventType) {
        try {
            System.out.println("--- Received an event of type: " + eventType + " ---");

            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            ReaderDetailsViewAMQP readerDetailsViewAMQP = objectMapper.readValue(json, ReaderDetailsViewAMQP.class);

            if (ReaderDetailsEvents.READER_DETAILS_CREATED.equals(eventType)) {
                readerService.create(readerDetailsViewAMQP); // Sync new reader details to local database
            } else if (ReaderDetailsEvents.READER_DETAILS_UPDATED.equals(eventType)) {
                readerService.update(readerDetailsViewAMQP); // Sync updated reader details
            }

            System.out.println("Event processed with the following info: " + readerDetailsViewAMQP);

        } catch (Exception e) {
            System.err.println("Error processing " + eventType + " Event: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "user.auth.queue")
    @SendTo("user.auth.response.queue")
    public String getAuthenticatedUserId(String username) {
        Optional<User> loggedUser = userService.findByUsername(username);
        Name name = loggedUser.map(User::getName).orElse(null);

        System.out.println("--- Sending User's full name: " + name + " ---");
        return name.toString();
    }
}