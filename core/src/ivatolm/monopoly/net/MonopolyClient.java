package ivatolm.monopoly.net;

import java.io.IOException;
import java.util.LinkedList;

import com.esotericsoftware.kryonet.Client;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqConnectEvent;
import ivatolm.monopoly.event.events.request.ReqConnectToLobbyEvent;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;

public class MonopolyClient implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private Client client;
    private volatile boolean running;

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
        if (events.isEmpty()) {
            return;
        }

        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ReqConnectToLobby:
                handleConnectLobby(event);
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

    private void handleConnectLobby(MonopolyEvent event) {
        ReqConnectToLobbyEvent e = (ReqConnectToLobbyEvent) event;

        setupClient();

        RespJoinedLobbyEvent joinedLobbyEvent = new RespJoinedLobbyEvent(client);
        try {
            client.connect(100, e.getIp(), 27841);
            client.sendTCP(new ReqConnectEvent());
        } catch (IOException e1) {
            joinedLobbyEvent.setErrorMsg(e.getErrorMsg());
        }
        joinedLobbyEvent.setResult(client.isConnected());

        EventDistributor.send(Endpoint.Client, e.getSender(), joinedLobbyEvent);
    }

    private void setupClient() {
        client = new Client();
        client.start();

        MonopolyProtocol.setupKryo(client.getKryo());
    }

}
