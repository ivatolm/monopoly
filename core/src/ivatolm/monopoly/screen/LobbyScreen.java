package ivatolm.monopoly.screen;

import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqInitPlayerEvent;
import ivatolm.monopoly.event.events.request.ReqInitObjectSocketEvent;

public class LobbyScreen extends BaseScreen {

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

        ReqInitPlayerEvent initPlayerEvent = null;
        try {
            initPlayerEvent = (ReqInitPlayerEvent) e.getObjectSocket().receive();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("Received player data: " + initPlayerEvent.getPlayer().getUUID());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
