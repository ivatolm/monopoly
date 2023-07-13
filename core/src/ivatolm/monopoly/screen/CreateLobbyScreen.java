package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisTextButton;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.navigation.GoLobbyScreenEvent;
import ivatolm.monopoly.event.events.request.ReqConnectToLobbyEvent;
import ivatolm.monopoly.event.events.request.ReqCreateLobbyEvent;
import ivatolm.monopoly.event.events.request.ReqInitClientEvent;
import ivatolm.monopoly.event.events.response.RespLobbyCreatedEvent;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;
import ivatolm.monopoly.widget.WidgetConstants;

public class CreateLobbyScreen extends BaseScreen {

    private VisTextButton connectButton;

    protected void generateUI() {
        connectButton = new VisTextButton("Create");

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.Server, new ReqCreateLobbyEvent());
            }
        });

        connectButton.setColor(Color.BLUE);
        connectButton.setFocusBorderEnabled(false);

        root.add(connectButton)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));

        stage.addActor(root);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case RespLobbyCreated:
                handleCreatedLobby(event);
                break;
            case RespJoinedLobby:
                handleJoinedLobby(event);
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

    private void handleCreatedLobby(MonopolyEvent event) {
        RespLobbyCreatedEvent e = (RespLobbyCreatedEvent) event;

        EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.Client, new ReqConnectToLobbyEvent(e.getIp()));
    }

    private void handleJoinedLobby(MonopolyEvent event) {
        RespJoinedLobbyEvent e = (RespJoinedLobbyEvent) event;

        EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.Game, new GoLobbyScreenEvent());

        EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.LobbyScreen,
                new ReqInitClientEvent(e.getClient()));
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
