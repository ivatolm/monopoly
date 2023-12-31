package ivatolm.monopoly.event.events.response;

import com.esotericsoftware.kryonet.Client;

import ivatolm.monopoly.event.MonopolyEvent;

public class RespJoinedLobbyEvent extends MonopolyEvent {

    private Client client;

    public RespJoinedLobbyEvent(Client client) {
        super(Type.RespJoinedLobby);

        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
