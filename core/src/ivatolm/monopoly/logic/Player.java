package ivatolm.monopoly.logic;

import java.util.UUID;

import ivatolm.monopoly.net.ObjectSocket;

public class Player {

    private UUID uuid;

    private ObjectSocket socket;

    public Player(ObjectSocket socket) {
        this.uuid = UUID.randomUUID();
        this.socket = socket;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ObjectSocket getSocket() {
        return socket;
    }

    public void dispose() {
        socket.dispose();
    }

}
