package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.widget.FlatWidgetFactory;

public class LobbyScreen extends BaseScreen {

    private Label infoLabel;

    protected void generateUI() {
        infoLabel = FlatWidgetFactory.FlatLabel("You are in the lobby");

        VerticalGroup column = new VerticalGroup();
        column.addActor(infoLabel);

        root.add(column);

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
