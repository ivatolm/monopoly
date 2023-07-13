package ivatolm.monopoly.event.events.net;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqDisconnectEvent extends MonopolyEvent {

    private transient Connection connection;

    public ReqDisconnectEvent() {
        super(Type.ReqDisconnectEvent);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

}