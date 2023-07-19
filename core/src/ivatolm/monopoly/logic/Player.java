package ivatolm.monopoly.logic;

import java.io.Serializable;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.events.net.ReqDisconnectEvent;
import lombok.Getter;
import lombok.Setter;

public class Player implements Serializable {

    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private transient Connection connection;

    private String name;

    @Getter
    @Setter
    private int position;

    public Player() {

    }

    public Player(Connection connection, String name) {
        this.id = null;
        this.connection = connection;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void dispose() {
        connection.sendTCP(new ReqDisconnectEvent());

        connection.close();
    }

}
