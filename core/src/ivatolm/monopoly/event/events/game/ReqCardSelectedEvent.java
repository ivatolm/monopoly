package ivatolm.monopoly.event.events.game;

import ivatolm.monopoly.event.MonopolyEvent;
import lombok.Getter;

public class ReqCardSelectedEvent extends MonopolyEvent {

    @Getter
    private int position;

    public ReqCardSelectedEvent(int position) {
        super(Type.ReqCardSelectedEvent);

        this.position = position;
    }

}
