package ivatolm.monopoly.event.events.request;

import com.esotericsoftware.kryonet.Client;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqInitClientEvent extends MonopolyEvent {

    private Client client;

    public ReqInitClientEvent(Client client) {
        super(Type.ReqInitClientEvent);

        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
