package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqConnectToLobbyEvent extends MonopolyEvent {

    private String ip;
    private String name;

    public ReqConnectToLobbyEvent(String ip, String name) {
        super(Type.ReqConnectToLobby);

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
