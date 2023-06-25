package ivatolm.monopoly.event.events;

import ivatolm.monopoly.event.MonopolyEvent;

public class JoinLobbyEvent extends MonopolyEvent {

    public JoinLobbyEvent() {
        super(Type.JoinLobby);
    }

}
