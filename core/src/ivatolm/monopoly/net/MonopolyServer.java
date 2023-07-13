package ivatolm.monopoly.net;

import java.io.IOException;
import java.util.LinkedList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqConnectEvent;
import ivatolm.monopoly.event.events.response.RespLobbyCreatedEvent;
import ivatolm.monopoly.logic.GameProperties;
import ivatolm.monopoly.logic.Lobby;
import ivatolm.monopoly.logic.Player;

public class MonopolyServer implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private Server server;
    private volatile boolean running;

    private Thread eventHandlerThread;

    private Lobby lobby;

    public MonopolyServer() {
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
            case ReqConnectEvent:
                handleConnectEvent(event);
                break;
            default:
                break;
        }
    }

    public void dispose() {
        running = false;

        try {
            eventHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateLobby(MonopolyEvent event) {
        setupServer();

        lobby = new Lobby(new GameProperties(2));

        EventDistributor.send(Endpoint.Server, Endpoint.CreateLobbyScreen,
                new RespLobbyCreatedEvent("127.0.0.1"));
    }

    private void handleConnectEvent(MonopolyEvent event) {
        ReqConnectEvent e = (ReqConnectEvent) event;

        lobby.addPlayer(new Player(e.getConnection()));
    }

    private void setupServer() {
        server = new Server();
        server.start();

        try {
            server.bind(27841);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MonopolyProtocol.setupKryo(server.getKryo());

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ReqConnectEvent) {
                    ReqConnectEvent request = (ReqConnectEvent) object;
                    request.setConnection(connection);

                    System.out.println("Connection request received");

                    EventDistributor.send(Endpoint.Server, Endpoint.Server, request);
                }
            }
        });
    }

}
