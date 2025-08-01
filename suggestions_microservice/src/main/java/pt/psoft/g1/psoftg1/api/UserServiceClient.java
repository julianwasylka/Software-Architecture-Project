package pt.psoft.g1.psoftg1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceClient {

    private final RabbitTemplate rabbitTemplate;

    public String getAuthenticatedUserName(String username) {
        return (String) rabbitTemplate.convertSendAndReceive("user.auth.queue", username);
    }
}
