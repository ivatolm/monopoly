package ivatolm.monopoly.net;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.EventReceiver.Type;
import ivatolm.monopoly.event.events.request.ConnectLobbyEvent;
import ivatolm.monopoly.event.events.response.ServerAcceptedEvent;

public class Client implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private Socket socket;
    private ClientSocketHandler socketHandler;

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
        System.out.println(event.getType());
        switch (event.getType()) {
            case ConnectLobby:
                handleConnectLobby(event);
                break;
            case ServerAccepted:
                handleServerAccepted(event);
            default:
                break;
        }
    }

    private void handleConnectLobby(MonopolyEvent event) {
        ConnectLobbyEvent e = (ConnectLobbyEvent) event;

        if (socketHandler != null) {
            socketHandler.dispose();
        }

        socketHandler = new ClientSocketHandler(e.getIp());
        socketHandler.start();
    }

    private void handleServerAccepted(MonopolyEvent event) {
        ServerAcceptedEvent e = (ServerAcceptedEvent) event;

        if (e.getResult()) {
            socket = e.getSocket();
        }

        ConnectLobbyEvent connectLobbyEvent = new ConnectLobbyEvent(null);
        connectLobbyEvent.setResult(e.getResult());
        connectLobbyEvent.setErrorMsg(e.getErrorMsg());

        EventDistributor.send(Type.JoinLobbyScreen, connectLobbyEvent);
    }

    public void dispose() {
        if (socket != null) {
            socket.dispose();
        }

        if (socketHandler != null) {
            socketHandler.dispose();
        }
    }

}

class ClientSocketHandler {

    private String ip;
    private Thread connectThread;

    ClientSocketHandler(String ip) {
        this.ip = ip;

        connectThread = new Thread() {
            @Override
            public void run() {
                connect();
            }
        };
    }

    void start() {
        connectThread.start();
    }

    void connect() {
        ServerAcceptedEvent event;

        try {
            Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ip, 26481, new SocketHints());
            event = new ServerAcceptedEvent(socket);
            event.setResult(true);
        } catch (GdxRuntimeException e) {
            event = new ServerAcceptedEvent(null);
            event.setResult(false);
            event.setErrorMsg("Couldn't connect to the host");
        }

        EventDistributor.send(Type.Client, event);
    }

    void dispose() {
        try {
            connectThread.join();
        } catch (InterruptedException e) {
            // doesn't matter
        }
    }

}
