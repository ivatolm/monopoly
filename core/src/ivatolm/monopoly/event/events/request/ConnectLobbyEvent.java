package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;

public class ConnectLobbyEvent extends MonopolyEvent {

    private String ip;

    public ConnectLobbyEvent(String ip) {
        super(Type.ConnectLobby);

        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

}
