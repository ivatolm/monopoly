package ivatolm.monopoly.event.events;

import ivatolm.monopoly.event.MonopolyEvent;

public class JoinedLobbyEvent extends MonopolyEvent {

    public JoinedLobbyEvent() {
        super(Type.JoinedLobby);
    }

}
