package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;

public class NetReqEndGameEvent extends MonopolyEvent {

    public NetReqEndGameEvent() {
        super(Type.NetReqEndGameEvent);
    }

}