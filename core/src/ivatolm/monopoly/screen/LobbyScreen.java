package ivatolm.monopoly.screen;

import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.EventReceiver.Endpoint;
import ivatolm.monopoly.event.events.net.ReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.event.events.request.ReqInitObjectSocketEvent;
import ivatolm.monopoly.net.ObjectSocket;

public class LobbyScreen extends BaseScreen {

    private NetworkThread networkThread;

    private VisLabel infoLabel;

    protected void generateUI() {
        infoLabel = new VisLabel("You are in the lobby");

        root.add(infoLabel);

        stage.addActor(root);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ReqInitObjectSocketEvent:
                handleInitPlayer(event);
                break;
            default:
                handleUpdateLobbyInfo(event);
                break;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        super.render(delta);
    }

    private void handleInitPlayer(MonopolyEvent event) {
        ReqInitObjectSocketEvent e = (ReqInitObjectSocketEvent) event;

        if (networkThread != null) {
            networkThread.dispose();

            try {
                networkThread.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        networkThread = new NetworkThread(e.getObjectSocket());
        networkThread.start();
    }

    private void handleUpdateLobbyInfo(MonopolyEvent event) {
        ReqUpdateLobbyInfoEvent e = (ReqUpdateLobbyInfoEvent) event;

        infoLabel.setText(e.getPlayerList().length);
    }

    @Override
    public void dispose() {
        super.dispose();

        networkThread.dispose();
        try {
            networkThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class NetworkThread extends Thread {

    private ObjectSocket socket;

    private volatile boolean running;

    public NetworkThread(ObjectSocket socket) {
        this.socket = socket;

        running = true;
    }

    @Override
    public void run() {
        while (running) {
            MonopolyEvent event = null;
            try {
                event = this.socket.receive();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EventDistributor.send(Endpoint.LobbyScreen, Endpoint.LobbyScreen, event);
        }
    }

    public void dispose() {
        running = false;
    }

}
