package ivatolm.monopoly.net;

import java.io.IOException;
import java.util.LinkedList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.NetReqConnectEvent;
import ivatolm.monopoly.event.events.net.NetReqDisconnectEvent;
import ivatolm.monopoly.event.events.request.ReqCreateLobbyEvent;
import ivatolm.monopoly.event.events.response.RespLobbyCreatedEvent;
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
        checkConnection();

        if (events.isEmpty()) {
            return;
        }

        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ReqCreateLobby:
                handleCreateLobby(event);
                break;
            case NetReqConnectEvent:
                handleConnectEvent(event);
                break;
            case NetReqDisconnectEvent:
                handleDisconnectEvent(event);
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

        if (server != null) {
            lobby.dispose();

            server.stop();

            try {
                server.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCreateLobby(MonopolyEvent event) {
        ReqCreateLobbyEvent e = (ReqCreateLobbyEvent) event;

        setupServer();

        lobby = new Lobby(e.getGameProperties());

        EventDistributor.send(Endpoint.Server, Endpoint.CreateLobbyScreen,
                new RespLobbyCreatedEvent("127.0.0.1", e.getName()));
    }

    private void handleConnectEvent(MonopolyEvent event) {
        NetReqConnectEvent e = (NetReqConnectEvent) event;

        lobby.addPlayer(new Player(e.getConnection(), e.getName()));
    }

    private void handleDisconnectEvent(MonopolyEvent event) {
        NetReqDisconnectEvent e = (NetReqDisconnectEvent) event;

        lobby.removePlayer(e.getConnection());
    }

    private void checkConnection() {
        if (server == null) {
            return;
        }

        for (Player player : lobby.getPlayerList()) {
            Connection connection = player.getConnection();

            if (!connection.isConnected()) {
                NetReqDisconnectEvent event = new NetReqDisconnectEvent();
                event.setConnection(connection);
                EventDistributor.send(Endpoint.Server, Endpoint.Server, event);
            }
        }
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
                if (object instanceof NetReqConnectEvent) {
                    NetReqConnectEvent request = (NetReqConnectEvent) object;
                    request.setConnection(connection);

                    EventDistributor.send(Endpoint.Server, Endpoint.Server, request);
                }

                if (object instanceof NetReqDisconnectEvent) {
                    NetReqDisconnectEvent request = (NetReqDisconnectEvent) object;
                    request.setConnection(connection);

                    EventDistributor.send(Endpoint.Server, Endpoint.Server, request);
                }
            }
        });
    }

}
