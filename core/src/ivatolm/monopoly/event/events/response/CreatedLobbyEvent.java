package ivatolm.monopoly.event.events.response;

import ivatolm.monopoly.event.MonopolyEvent;

public class CreatedLobbyEvent extends MonopolyEvent {

    private String ip;

    public CreatedLobbyEvent(String ip) {
        super(Type.CreatedLobby);

        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

}