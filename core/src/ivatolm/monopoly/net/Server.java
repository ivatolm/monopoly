package ivatolm.monopoly.net;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.EventReceiver.Type;
import ivatolm.monopoly.event.events.ClientConnectedEvent;

public class Server implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private ServerSocket socket;
    private ServerSocketHandler socketHandler;

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
            case StartLobby:
                handleStartLobby(event);
            case ClientConnected:
                handleClientConnected(event);
            default:
                break;
        }
    }

    private void handleStartLobby(MonopolyEvent event) {
        if (socket != null) {
            socket.dispose();
        }

        if (socketHandler != null) {
            socketHandler.dispose();
        }

        socket = Gdx.net.newServerSocket(Protocol.TCP, 26481, new ServerSocketHints());
        socketHandler = new ServerSocketHandler(socket);
        socketHandler.start();
    }

    private void handleClientConnected(MonopolyEvent event) {
        ClientConnectedEvent e = (ClientConnectedEvent) event;

        Socket client = e.getSocket();
        System.out.println(client.isConnected());
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

class ServerSocketHandler {

    private volatile boolean run;
    private ServerSocket socket;
    private Thread acceptThread;

    ServerSocketHandler(ServerSocket socket) {
        this.socket = socket;

        acceptThread = new Thread() {
            @Override
            public void run() {
                accept();
            }
        };

        run = true;
    }

    void start() {
        acceptThread.start();
    }

    void accept() {
        while (run) {
            Socket clientSocket = socket.accept(new SocketHints());
            EventDistributor.send(Type.Server, new ClientConnectedEvent(clientSocket));
        }
    }

    void dispose() {
        run = false;

        try {
            acceptThread.join();
        } catch (InterruptedException e) {
            // doesn't matter
        }
    }

}
