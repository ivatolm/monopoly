package ivatolm.monopoly.event.events.net;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;

public class ReqConnectEvent extends MonopolyEvent {

    private transient Connection connection;
    private String name;

    public ReqConnectEvent() {
        super(Type.ReqConnectEvent);
    }

    public ReqConnectEvent(String name) {
        super(Type.ReqConnectEvent);

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
