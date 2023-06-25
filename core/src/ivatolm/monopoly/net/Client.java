package ivatolm.monopoly.net;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ivatolm.monopoly.event.ConnectLobbyEvent;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;

public class Client implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private Socket socket;

    @Override
    public void receive(MonopolyEvent event) {
        events.add(event);
    }

    @Override
    public void handleEvents() {
        if (events.isEmpty()) {
            return;
        }

        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ConnectLobby:
                connectLobby(event);
                break;
            default:
                break;
        }
    }

    private void connectLobby(MonopolyEvent event) {
        ConnectLobbyEvent e = (ConnectLobbyEvent) event;

        try {
            socket = Gdx.net.newClientSocket(Protocol.TCP, e.getIp(), 26481, new SocketHints());
        } catch (GdxRuntimeException exception) {
            e.setErrorMsg("Cannot connect to the host");
            EventDistributor.send(Type.JoinLobbyScreen, e);
        }

    }

}
