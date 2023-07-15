package ivatolm.monopoly.net;

import java.io.IOException;
import java.util.LinkedList;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.navigation.GoJoinLobbyScreenEvent;
import ivatolm.monopoly.event.events.net.ReqConnectEvent;
import ivatolm.monopoly.event.events.net.ReqDisconnectEvent;
import ivatolm.monopoly.event.events.net.RespConnectEvent;
import ivatolm.monopoly.event.events.request.ReqConnectToLobbyEvent;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;

public class MonopolyClient implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private Client client;
    private volatile boolean running;

    private volatile boolean waitingResponse;
    private volatile Object response;

    private Thread eventHandlerThread;

    public MonopolyClient() {
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
            case ReqConnectToLobby:
                handleConnectLobby(event);
                break;
            case ReqDisconnectEvent:
                handleDisconnect(event);
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

        if (client != null) {
            if (client.isConnected()) {
                // client.sendTCP(new ReqDisconnectEvent());
            }

            client.stop();

            try {
                client.dispose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleConnectLobby(MonopolyEvent event) {
        ReqConnectToLobbyEvent e = (ReqConnectToLobbyEvent) event;

        setupClient();

        final Endpoint sender = e.getSender();
        Listener listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof RespConnectEvent) {
                    RespConnectEvent resp = (RespConnectEvent) object;

                    response = resp;
                    waitingResponse = false;
                }
            }
        };

        waitingResponse = true;
        client.addListener(listener);

        try {
            client.connect(100, e.getIp(), 27841);
            client.sendTCP(new ReqConnectEvent(e.getName()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        client.removeListener(listener);

        RespJoinedLobbyEvent joinedLobbyEvent = new RespJoinedLobbyEvent(client);
        if (waitingResponse) {
            joinedLobbyEvent.setResult(false);
            joinedLobbyEvent.setErrorMsg("Connection timed out");

            closeClient();
        } else {
            MonopolyEvent resp = (MonopolyEvent) response;
            joinedLobbyEvent.setResult(resp.getResult());
            joinedLobbyEvent.setErrorMsg(resp.getErrorMsg());

            if (!resp.getResult()) {
                closeClient();
            }
        }

        EventDistributor.send(Endpoint.Client, sender, joinedLobbyEvent);
    }

    private void handleDisconnect(MonopolyEvent event) {
        @SuppressWarnings("unused")
        ReqDisconnectEvent e = (ReqDisconnectEvent) event;

        closeClient();

        EventDistributor.send(Endpoint.Client, Endpoint.Game, new GoJoinLobbyScreenEvent());
    }

    private void checkConnection() {
        if (client == null) {
            return;
        }

        if (!client.isConnected()) {
            EventDistributor.send(Endpoint.Client, Endpoint.Client, new ReqDisconnectEvent());
        }
    }

    private void setupClient() {
        client = new Client();
        client.start();

        MonopolyProtocol.setupKryo(client.getKryo());

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ReqDisconnectEvent) {
                    ReqDisconnectEvent request = (ReqDisconnectEvent) object;
                    request.setConnection(connection);

                    EventDistributor.send(Endpoint.Client, Endpoint.Client, request);
                }
            }
        });
    }

    private void closeClient() {
        client.stop();
        try {
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client = null;
    }

}
