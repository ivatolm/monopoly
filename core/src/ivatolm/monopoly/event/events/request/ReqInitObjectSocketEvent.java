package ivatolm.monopoly.event.events.request;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.net.ObjectSocket;

public class ReqInitObjectSocketEvent extends MonopolyEvent {

    private ObjectSocket objectSocket;

    public ReqInitObjectSocketEvent(ObjectSocket objectSocket) {
        super(Type.ReqInitObjectSocketEvent);

        this.objectSocket = objectSocket;
    }

    public ObjectSocket getObjectSocket() {
        return objectSocket;
    }

}
