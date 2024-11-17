package com.buehler.server.websocketservice.controller;

import com.buehler.server.websocketservice.model.GameStatusMessage;
import com.buehler.server.websocketservice.model.PowerUpNotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/outcome")
    public void sendGameStatus(@RequestBody GameStatusMessage gameStatus) {
        messagingTemplate.convertAndSend("/topic/game/outcome", gameStatus);
    }

    @PostMapping("/power-up-applied")
    public void sendPowerUpAppliedMessage(@RequestBody PowerUpNotificationMessage powerUpNotificationMessage) {
        messagingTemplate.convertAndSend("/topic/power-up-applied", powerUpNotificationMessage);
    }
}
