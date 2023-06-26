package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqConnectToLobbyEvent extends MonopolyEvent {

    private String ip;

    public ReqConnectToLobbyEvent(String ip) {
        super(Type.ReqConnectToLobby);

        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

}
