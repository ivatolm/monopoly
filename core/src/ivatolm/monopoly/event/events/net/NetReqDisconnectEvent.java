package ivatolm.monopoly.event.events.net;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;

public class NetReqDisconnectEvent extends MonopolyEvent {

    private transient Connection connection;

    public NetReqDisconnectEvent() {
        super(Type.NetReqDisconnectEvent);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

}