package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.events.navigation.GoCreateLobbyScreenEvent;
import ivatolm.monopoly.event.events.navigation.GoJoinLobbyScreenEvent;
import ivatolm.monopoly.widget.FlatWidgetFactory;

public class MainMenuScreen extends BaseScreen {

    private TextButton joinLobbyButton;
    private TextButton createLobbyButton;

    protected void generateUI() {
        joinLobbyButton = FlatWidgetFactory.FlatButton("Join lobby", Color.GRAY, 100, 50);
        createLobbyButton = FlatWidgetFactory.FlatButton("Create lobby", Color.GRAY, 100, 50);

        joinLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Type.MainMenuScreen, Type.Game, new GoJoinLobbyScreenEvent());
            }
        });

        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Type.MainMenuScreen, Type.Game, new GoCreateLobbyScreenEvent());
            }
        });

        VerticalGroup column = new VerticalGroup();
        column.addActor(joinLobbyButton);
        column.addActor(createLobbyButton);

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
