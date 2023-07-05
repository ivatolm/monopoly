package ivatolm.monopoly.event.events.response;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class RespJoinedLobbyEvent extends MonopolyEvent {

    private Player player;

    public RespJoinedLobbyEvent(Player player) {
        super(Type.RespJoinedLobby);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
