package ivatolm.monopoly.event.events.response;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.net.ObjectSocket;

public class RespJoinedLobbyEvent extends MonopolyEvent {

    private ObjectSocket objectSocket;

    public RespJoinedLobbyEvent(ObjectSocket objectSocket) {
        super(Type.RespJoinedLobby);

        this.objectSocket = objectSocket;
    }

    public ObjectSocket getObjectSocket() {
        return objectSocket;
    }

}
