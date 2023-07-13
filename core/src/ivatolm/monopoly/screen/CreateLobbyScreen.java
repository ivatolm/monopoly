package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

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

    private VisLabel nameLabel;
    private VisTextField nameTextField;
    private VisTextButton connectButton;
    private VisLabel errorMessageLabel;

    protected void generateUI() {
        nameLabel = new VisLabel("Name: ");
        nameTextField = new VisTextField();
        connectButton = new VisTextButton("Create");
        errorMessageLabel = new VisLabel("", Color.RED);

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorMessageLabel.setText("");

                String name = nameTextField.getText();
                if (name.isBlank()) {
                    errorMessageLabel.setText("Name cannot be blank");
                    return;
                }

                name = name.strip();

                EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.Server,
                        new ReqCreateLobbyEvent(name));
            }
        });

        nameTextField.setFocusBorderEnabled(false);

        connectButton.setColor(Color.BLUE);
        connectButton.setFocusBorderEnabled(false);

        root.add(nameLabel).colspan(1);
        root.add(nameTextField).colspan(1)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH * 1.5f, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));
        root.row();
        root.add(connectButton).colspan(2)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));
        root.row();
        root.add(errorMessageLabel).colspan(2);

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

        EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.Client,
                new ReqConnectToLobbyEvent(e.getIp(), e.getName()));
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
