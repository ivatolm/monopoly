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
import ivatolm.monopoly.logic.GameProperties;
import ivatolm.monopoly.logic.Lobby;
import ivatolm.monopoly.logic.Player;

public class Server implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private ServerSocket socket;
    private volatile boolean running;

    private Thread eventHandlerThread;
    private AcceptThread acceptThread;

    private Lobby lobby;

    public Server() {
        eventHandlerThread = new Thread() {
            @Override
            public void run() {
                while (running) {
                    handleEvents();

                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        running = true;
        eventHandlerThread.start();
    }

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

            acceptThread.dispose();
            try {
                acceptThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket = Gdx.net.newServerSocket(Protocol.TCP, "127.0.0.1", 26481, new ServerSocketHints());
        acceptThread = new AcceptThread(socket);
        acceptThread.start();

        if (lobby != null) {
            lobby.dispose();
        }

        lobby = new Lobby(new GameProperties(2));

        EventDistributor.send(Endpoint.Server, Endpoint.CreateLobbyScreen, new RespLobbyCreatedEvent("127.0.0.1"));
    }

    private void handleClientConnected(MonopolyEvent event) {
        RespClientConnectedEvent e = (RespClientConnectedEvent) event;

        Socket client = e.getSocket();
        boolean result = lobby.addPlayer(new Player(new ObjectSocket(client)));

        // MonopolyEvent updateLobbyEvent = new
        // ReqUpdateLobbyInfoEvent(lobby.getPlayerList());
        // for (Player player : lobby.getPlayerList()) {
        // ObjectSocket socket = player.getSocket();

        // try {
        // socket.send(updateLobbyEvent);
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // }
        // }
    }

    public void dispose() {
        running = false;

        try {
            eventHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (socket != null) {
            socket.dispose();

            acceptThread.dispose();
            try {
                acceptThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class AcceptThread extends Thread {

    private ServerSocket socket;

    private volatile boolean running;

    public AcceptThread(ServerSocket socket) {
        this.socket = socket;

        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket clientSocket = socket.accept(new SocketHints());
                EventDistributor.send(Endpoint.Server, Endpoint.Server, new RespClientConnectedEvent(clientSocket));
            } catch (GdxRuntimeException e) {
                // timeout expired
            }
        }
    }

    public void dispose() {
        running = false;
    }

}
