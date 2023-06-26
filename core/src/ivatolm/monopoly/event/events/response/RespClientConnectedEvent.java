package ivatolm.monopoly.event.events.response;

import com.badlogic.gdx.net.Socket;

import ivatolm.monopoly.event.MonopolyEvent;

public class RespClientConnectedEvent extends MonopolyEvent {

    private Socket socket;

    public RespClientConnectedEvent(Socket socket) {
        super(Type.RespClientConnected);

        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

}
