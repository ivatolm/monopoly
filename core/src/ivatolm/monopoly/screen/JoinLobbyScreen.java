package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.ConnectLobbyEvent;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.widget.FlatWidgetFactory;

public class JoinLobbyScreen extends BaseScreen {

    protected void generateUI() {
        TextField ipTextField = FlatWidgetFactory.FlatTextField(Color.GRAY, 100, 50);

        TextButton connectButton = FlatWidgetFactory.FlatButton("Connect", Color.GREEN, 100, 50);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Type.Game, new ConnectLobbyEvent("192.168.0.1"));
            }
        });

        VerticalGroup column = new VerticalGroup();
        column.addActor(ipTextField);
        column.addActor(connectButton);

        root.add(column);

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
