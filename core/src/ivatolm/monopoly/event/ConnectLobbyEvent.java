package ivatolm.monopoly.event;

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
