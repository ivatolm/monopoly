package ivatolm.monopoly.logic;

import java.util.UUID;

import com.badlogic.gdx.net.Socket;

public class Player {

    private UUID uuid;

    private Socket socket;

    public Player(Socket socket) {
        this.uuid = UUID.randomUUID();
        this.socket = socket;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Socket getSocket() {
        return socket;
    }

}
