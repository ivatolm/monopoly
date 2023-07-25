package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import lombok.Getter;

public class NetReqPledgeEvent extends MonopolyEvent {

    @Getter
    private int position;

    public NetReqPledgeEvent() {
        super(Type.NetReqPledgeEvent);
    }

    public NetReqPledgeEvent(int position) {
        super(Type.NetReqPledgeEvent);

        this.position = position;
    }

}
