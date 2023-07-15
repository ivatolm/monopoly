package ivatolm.monopoly.logic;

import java.io.Serializable;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.events.net.ReqDisconnectEvent;

public class Player implements Serializable {

    private String uuid;
    private transient Connection connection;

    private String name;

    public Player() {

    }

    public Player(Connection connection, String name) {
        this.uuid = UUID.randomUUID().toString();
        this.connection = connection;
        this.name = name;
    }

    public String getUUID() {
        return uuid;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    public void dispose() {
        connection.sendTCP(new ReqDisconnectEvent());

        connection.close();
    }

}
