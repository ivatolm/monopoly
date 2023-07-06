package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class ReqUpdateLobbyInfoEvent extends MonopolyEvent {

    private Player[] players;

    public ReqUpdateLobbyInfoEvent(Player[] players) {
        super(Type.ReqUpdateLobbyInfoEvent);

        this.players = players;
    }

    public Player[] getPlayerList() {
        return players;
    }

}
