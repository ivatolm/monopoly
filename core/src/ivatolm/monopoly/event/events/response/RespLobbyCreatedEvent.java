package ivatolm.monopoly.event.events.response;

import ivatolm.monopoly.event.MonopolyEvent;

public class RespLobbyCreatedEvent extends MonopolyEvent {

    private String ip;
    private String name;

    public RespLobbyCreatedEvent(String ip, String name) {
        super(Type.RespLobbyCreated);

        this.ip = ip;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

}