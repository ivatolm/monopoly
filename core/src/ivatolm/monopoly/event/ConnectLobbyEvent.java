package ivatolm.monopoly.event;

public class ConnectLobbyEvent extends MonopolyEvent {

    private String ip;

    public ConnectLobbyEvent(String ip) {
        super(Type.ConnectLobby);
    }

    public String getIp() {
        return ip;
    }

}
