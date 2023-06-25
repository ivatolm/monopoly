package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;

public class CreateLobbyEvent extends MonopolyEvent {

    public CreateLobbyEvent() {
        super(Type.CreateLobby);
    }

}
