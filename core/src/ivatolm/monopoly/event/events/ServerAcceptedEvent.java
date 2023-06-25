package ivatolm.monopoly.event.events;

import com.badlogic.gdx.net.Socket;

import ivatolm.monopoly.event.MonopolyEvent;

public class ServerAcceptedEvent extends MonopolyEvent {

    private Socket socket;

    public ServerAcceptedEvent(Socket socket) {
        super(Type.ServerAccepted);

        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

}