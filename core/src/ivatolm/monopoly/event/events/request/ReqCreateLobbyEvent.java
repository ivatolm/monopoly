package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.GameProperties;
import lombok.Getter;

public class ReqCreateLobbyEvent extends MonopolyEvent {

    @Getter
    private String name;

    @Getter
    private GameProperties gameProperties;

    public ReqCreateLobbyEvent(String name, GameProperties gameProperties) {
        super(Type.ReqCreateLobby);

        this.name = name;
        this.gameProperties = gameProperties;
    }

}
