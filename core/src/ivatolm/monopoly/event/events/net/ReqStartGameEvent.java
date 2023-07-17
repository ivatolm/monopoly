package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class ReqStartGameEvent extends MonopolyEvent {

    private Player player;

    public ReqStartGameEvent() {
        super(Type.ReqStartGameEvent);
    }

    public ReqStartGameEvent(Player player) {
        super(Type.ReqStartGameEvent);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
