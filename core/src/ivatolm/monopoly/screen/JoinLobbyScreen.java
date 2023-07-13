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
import ivatolm.monopoly.event.events.request.ReqInitClientEvent;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;
import ivatolm.monopoly.widget.WidgetConstants;

public class JoinLobbyScreen extends BaseScreen {

    private VisLabel nameLabel;
    private VisTextField nameTextField;
    private VisLabel ipLabel;
    private VisTextField ipTextField;
    private VisTextButton connectButton;
    private VisLabel errorMessageLabel;

    protected void generateUI() {
        nameLabel = new VisLabel("Name: ");
        nameTextField = new VisTextField();

        ipLabel = new VisLabel("IP: ");
        ipTextField = new VisTextField();
        connectButton = new VisTextButton("Connect");
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

                EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.Client,
                        new ReqConnectToLobbyEvent(ipTextField.getText(), name));
            }
        });

        nameTextField.setFocusBorderEnabled(false);
        ipTextField.setFocusBorderEnabled(false);

        connectButton.setColor(Color.BLUE);
        connectButton.setFocusBorderEnabled(false);

        root.add(nameLabel).colspan(1);
        root.add(nameTextField).colspan(1)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH * 1.5f, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));
        root.row();
        root.add(ipLabel).colspan(1);
        root.add(ipTextField).colspan(2)
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

    @Override
    public void dispose() {
        super.dispose();
    }

    private void handleJoinedLobby(MonopolyEvent event) {
        if (event.getResult()) {
            RespJoinedLobbyEvent e = (RespJoinedLobbyEvent) event;

            EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.Game, new GoLobbyScreenEvent());
            EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.LobbyScreen,
                    new ReqInitClientEvent(e.getClient()));
        } else {
            String errorMsg = event.getErrorMsg();

            errorMessageLabel.setText(errorMsg);
        }
    }

}
