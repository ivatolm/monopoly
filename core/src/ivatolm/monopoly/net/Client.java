package ivatolm.monopoly.net;

import java.util.LinkedList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import ivatolm.monopoly.event.ConnectLobbyEvent;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;

public class Client extends ApplicationAdapter implements EventReceiver {

    private LinkedList<MonopolyEvent> events;

    private Socket socket;

    @Override
    public void create() {
        events = new LinkedList<>();
    }

    @Override
    public void receive(MonopolyEvent event) {
        events.add(event);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ConnectLobby:
                connectLobby(event);
                break;
            default:
                break;
        }
    }

    @Override
    public void render() {
        if (events.size() > 0) {
            handleEvents();
        }
    }

    private void connectLobby(MonopolyEvent event) {
        ConnectLobbyEvent e = (ConnectLobbyEvent) event;

        if (socket != null) {
            socket.dispose();
        }

        socket = Gdx.net.newClientSocket(Protocol.TCP, e.getIp(), 26481, new SocketHints());
    }

}
