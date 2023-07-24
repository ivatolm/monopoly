package ivatolm.monopoly.event.events.net;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;

public class NetReqConnectEvent extends MonopolyEvent {

    private transient Connection connection;
    private String name;

    public NetReqConnectEvent() {
        super(Type.NetReqConnectEvent);
    }

    public NetReqConnectEvent(String name) {
        super(Type.NetReqConnectEvent);

        this.name = name;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

}
