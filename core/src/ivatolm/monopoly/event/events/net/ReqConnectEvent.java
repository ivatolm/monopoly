package ivatolm.monopoly.event.events.net;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqConnectEvent extends MonopolyEvent {

    private transient Connection connection;

    public ReqConnectEvent() {
        super(Type.ReqConnectEvent);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

}
