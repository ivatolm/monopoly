package ivatolm.monopoly.net;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.EventReceiver.Endpoint;
import ivatolm.monopoly.event.events.response.RespClientConnectedEvent;
import ivatolm.monopoly.event.events.response.RespLobbyCreatedEvent;

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
            case ReqCreateLobby:
                handleCreateLobby(event);
                break;
            case RespClientConnected:
                handleClientConnected(event);
                break;
            default:
                break;
        }
    }

    private void handleCreateLobby(MonopolyEvent event) {
        if (socket != null) {
            socket.dispose();
        }

        if (socketHandler != null) {
            socketHandler.dispose();
        }

        socket = Gdx.net.newServerSocket(Protocol.TCP, "127.0.0.1", 26481, new ServerSocketHints());
        socketHandler = new ServerSocketHandler(socket);
        socketHandler.start();

        EventDistributor.send(Endpoint.Server, Endpoint.CreateLobbyScreen, new RespLobbyCreatedEvent("127.0.0.1"));
    }

    private void handleClientConnected(MonopolyEvent event) {
        RespClientConnectedEvent e = (RespClientConnectedEvent) event;

        Socket client = e.getSocket();
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
            try {
                Socket clientSocket = socket.accept(new SocketHints());
                EventDistributor.send(Endpoint.Server, Endpoint.Server, new RespClientConnectedEvent(clientSocket));
            } catch (GdxRuntimeException e) {
                // timeout expired
            }
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
