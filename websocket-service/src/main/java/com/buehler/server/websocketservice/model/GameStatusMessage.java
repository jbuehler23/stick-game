package com.buehler.server.websocketservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameStatusMessage {
    private Long gameId;
    private String status;
    private String winner;

}
