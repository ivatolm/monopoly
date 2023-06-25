package ivatolm.monopoly.event.events;

import com.badlogic.gdx.net.Socket;

import ivatolm.monopoly.event.MonopolyEvent;

public class ClientConnectedEvent extends MonopolyEvent {

    private Socket socket;

    public ClientConnectedEvent(Socket socket) {
        super(Type.ClientConnected);

        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

}
