package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisTextButton;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.events.navigation.GoCreateLobbyScreenEvent;
import ivatolm.monopoly.event.events.navigation.GoJoinLobbyScreenEvent;
import ivatolm.monopoly.widget.WidgetConstants;

public class MainMenuScreen extends BaseScreen {

    private VisTextButton joinLobbyButton;
    private VisTextButton createLobbyButton;

    protected void generateUI() {
        joinLobbyButton = new VisTextButton("Join lobby");
        createLobbyButton = new VisTextButton("Create lobby");

        joinLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.MainMenuScreen, Endpoint.Game, new GoJoinLobbyScreenEvent());
            }
        });

        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.MainMenuScreen, Endpoint.Game, new GoCreateLobbyScreenEvent());
            }
        });

        root.add(joinLobbyButton)
                .width(WidgetConstants.BUTTON_WIDTH)
                .height(WidgetConstants.BUTTON_HEIGHT);
        root.row();
        root.add(createLobbyButton)
                .width(WidgetConstants.BUTTON_WIDTH)
                .height(WidgetConstants.BUTTON_HEIGHT);

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
