package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class NetReqUpdateLobbyInfoEvent extends MonopolyEvent {

    private Player[] players;

    public NetReqUpdateLobbyInfoEvent() {
        super(Type.NetReqUpdateLobbyInfoEvent);
    }

    public NetReqUpdateLobbyInfoEvent(Player[] players) {
        super(Type.NetReqUpdateLobbyInfoEvent);

        this.players = players;
    }

    public Player[] getPlayerList() {
        return players;
    }

}
