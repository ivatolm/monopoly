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
import ivatolm.monopoly.event.EventReceiver.Endpoint;
import ivatolm.monopoly.event.events.request.ReqConnectToLobbyEvent;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;
import ivatolm.monopoly.logic.Player;
import ivatolm.monopoly.event.events.response.RespClientAcceptedEvent;

public class Client implements EventReceiver {

    private LinkedList<MonopolyEvent> events = new LinkedList<>();

    private Socket socket;
    private volatile boolean running;

    private Thread eventHandlerThread;
    private Thread connectThread;

    private EventReceiver.Endpoint sender;

    public Client() {
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
            case RespClientAccepted:
                handleClientAccepted(event);
                break;
            default:
                break;
        }
    }

    private void handleConnectLobby(MonopolyEvent event) {
        ReqConnectToLobbyEvent e = (ReqConnectToLobbyEvent) event;
        sender = e.getSender();

        connectThread = new ConnectThread(e.getIp());
        connectThread.start();
    }

    private void handleClientAccepted(MonopolyEvent event) {
        try {
            connectThread.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        RespClientAcceptedEvent e = (RespClientAcceptedEvent) event;

        Player player = null;
        if (e.getResult()) {
            socket = e.getSocket();
            player = new Player(new ObjectSocket(socket));
        }

        RespJoinedLobbyEvent joinedLobbyEvent = new RespJoinedLobbyEvent(player);
        joinedLobbyEvent.setResult(e.getResult());
        joinedLobbyEvent.setErrorMsg(e.getErrorMsg());

        EventDistributor.send(Endpoint.Client, sender, joinedLobbyEvent);
        sender = null;
    }

    public void dispose() {
        running = false;

        try {
            eventHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class ConnectThread extends Thread {

    private final String ip;

    public ConnectThread(String ip) {
        super();

        this.ip = ip;
    }

    @Override
    public void run() {
        RespClientAcceptedEvent event;

        try {
            Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ip, 26481, new SocketHints());
            event = new RespClientAcceptedEvent(socket);
            event.setResult(true);
        } catch (GdxRuntimeException e) {
            event = new RespClientAcceptedEvent(null);
            event.setResult(false);
            event.setErrorMsg("Couldn't connect to the host");
        }

        EventDistributor.send(Endpoint.Client, Endpoint.Client, event);
    }

}
