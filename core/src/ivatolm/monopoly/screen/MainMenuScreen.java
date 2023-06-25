package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.events.JoinLobbyEvent;
import ivatolm.monopoly.widget.FlatWidgetFactory;

public class MainMenuScreen extends BaseScreen {

    protected void generateUI() {
        TextButton button = FlatWidgetFactory.FlatButton("Join game", Color.GRAY, 100, 50);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Type.Game, new JoinLobbyEvent());
            }
        });
        root.add(button);

        stage.addActor(root);
    }

    @Override
    public void handleEvents() {

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
