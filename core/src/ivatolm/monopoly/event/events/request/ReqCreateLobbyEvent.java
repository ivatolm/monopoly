package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqCreateLobbyEvent extends MonopolyEvent {

    private String name;

    public ReqCreateLobbyEvent(String name) {
        super(Type.ReqCreateLobby);

        this.name = name;
    }

    public String getName() {
        return name;
    }

}
