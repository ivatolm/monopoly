package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqStartGameEvent extends MonopolyEvent {

    private String uuid;

    public ReqStartGameEvent() {
        super(Type.ReqStartGameEvent);
    }

    public ReqStartGameEvent(String uuid) {
        super(Type.ReqStartGameEvent);

        this.uuid = uuid;
    }

    public String getUUID() {
        return uuid;
    }

}
