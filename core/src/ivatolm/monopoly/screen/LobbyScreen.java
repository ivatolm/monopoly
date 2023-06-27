package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;

import ivatolm.monopoly.event.MonopolyEvent;

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
            default:
                break;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
