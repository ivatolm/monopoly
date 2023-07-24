package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class NetReqStartGameEvent extends MonopolyEvent {

    private Player player;

    public NetReqStartGameEvent() {
        super(Type.NetReqStartGameEvent);
    }

    public NetReqStartGameEvent(Player player) {
        super(Type.NetReqStartGameEvent);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
