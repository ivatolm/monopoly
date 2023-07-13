package ivatolm.monopoly.logic;

import java.io.Serializable;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class Player implements Serializable {

    private String uuid;

    private transient Connection connection;

    public Player() {

    }

    public Player(Connection connection) {
        this.uuid = UUID.randomUUID().toString();
        this.connection = connection;
    }

    public String getUUID() {
        return uuid;
    }

    public Connection getConnection() {
        return connection;
    }

    public void dispose() {
        connection.close();
    }

}
