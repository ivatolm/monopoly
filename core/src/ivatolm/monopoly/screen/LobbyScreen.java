package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.request.ReqInitPlayerEvent;

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
            case ReqInitPlayerEvent:
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
        ReqInitPlayerEvent e = (ReqInitPlayerEvent) event;

        System.out.println("Player 'generated' UUID:" + e.getPlayer().getUUID());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
