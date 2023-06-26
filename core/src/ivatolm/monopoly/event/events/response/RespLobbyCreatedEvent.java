package ivatolm.monopoly.event.events.response;

import ivatolm.monopoly.event.MonopolyEvent;

public class RespLobbyCreatedEvent extends MonopolyEvent {

    private String ip;

    public RespLobbyCreatedEvent(String ip) {
        super(Type.RespLobbyCreated);

        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

}