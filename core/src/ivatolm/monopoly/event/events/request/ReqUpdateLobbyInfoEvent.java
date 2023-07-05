package ivatolm.monopoly.event.events.request;

import java.util.Collection;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class ReqUpdateLobbyInfoEvent extends MonopolyEvent {

    private Collection<Player> players;

    public ReqUpdateLobbyInfoEvent(Collection<Player> players) {
        super(Type.ReqUpdateLobbyInfoEvent);
    }

    public Collection<Player> getPlayerList() {
        return players;
    }

}
