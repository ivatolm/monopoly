package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.widget.FlatButtonFactory;

public class MainMenuScreen extends BaseScreen {

    protected void generateUI() {
        TextButton button = FlatButtonFactory.FlatButton("Join game", Color.GRAY, 100, 50);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Type.Game, MonopolyEvent.JoinLobby);
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
        super.render(delta);

        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
