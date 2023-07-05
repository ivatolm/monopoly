package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;

public class ReqInitPlayerEvent extends MonopolyEvent {

    private Player player;

    public ReqInitPlayerEvent(Player player) {
        super(Type.ReqInitPlayerEvent);

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
