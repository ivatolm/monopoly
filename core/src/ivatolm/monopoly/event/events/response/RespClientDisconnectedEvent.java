package ivatolm.monopoly.event.events.response;

import com.badlogic.gdx.net.Socket;

import ivatolm.monopoly.event.MonopolyEvent;

public class RespClientDisconnectedEvent extends MonopolyEvent {

    private Socket socket;

    public RespClientDisconnectedEvent(Socket socket) {
        super(Type.RespClientDisconnected);

        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

}

