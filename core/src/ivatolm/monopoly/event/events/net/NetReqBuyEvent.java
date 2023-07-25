package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import lombok.Getter;

public class NetReqBuyEvent extends MonopolyEvent {

    @Getter
    private int position;

    public NetReqBuyEvent() {
        super(Type.NetReqBuyEvent);
    }

    public NetReqBuyEvent(int position) {
        super(Type.NetReqBuyEvent);

        this.position = position;
    }

}
