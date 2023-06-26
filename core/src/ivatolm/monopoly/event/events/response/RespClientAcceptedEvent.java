package ivatolm.monopoly.event.events.response;

import com.badlogic.gdx.net.Socket;

import ivatolm.monopoly.event.MonopolyEvent;

public class RespClientAcceptedEvent extends MonopolyEvent {

    private Socket socket;

    public RespClientAcceptedEvent(Socket socket) {
        super(Type.RespClientAccepted);

        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

}