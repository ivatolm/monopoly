package ivatolm.monopoly.event.events.response;

import ivatolm.monopoly.event.MonopolyEvent;

public class JoinedLobbyEvent extends MonopolyEvent {

    public JoinedLobbyEvent() {
        super(Type.JoinedLobby);
    }

}
