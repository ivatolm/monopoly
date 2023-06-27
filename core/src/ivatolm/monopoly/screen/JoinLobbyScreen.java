package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.navigation.GoLobbyScreenEvent;
import ivatolm.monopoly.event.events.request.ReqConnectToLobbyEvent;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;
import ivatolm.monopoly.widget.WidgetConstants;

public class JoinLobbyScreen extends BaseScreen {

    private VisTextField ipTextField;
    private VisTextButton connectButton;
    private VisLabel errorMessageLabel;

    protected void generateUI() {
        ipTextField = new VisTextField();
        connectButton = new VisTextButton("Connect");
        errorMessageLabel = new VisLabel("", Color.RED);

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorMessageLabel.setText("");
                EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.Client,
                        new ReqConnectToLobbyEvent(ipTextField.getText()));
            }
        });

        ipTextField.setFocusBorderEnabled(false);

        connectButton.setColor(Color.BLUE);
        connectButton.setFocusBorderEnabled(false);

        root.add(ipTextField).colspan(1)
                .height(WidgetConstants.BUTTON_HEIGHT);
        root.add(connectButton).colspan(1)
                .width(WidgetConstants.BUTTON_WIDTH)
                .height(WidgetConstants.BUTTON_HEIGHT);
        root.row();
        root.add(errorMessageLabel).colspan(2);

        stage.addActor(root);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        System.out.println(event);
        switch (event.getType()) {
            case RespJoinedLobby:
                if (event.getResult()) {
                    handleJoinedSuccess(event);
                } else {
                    handleJoinedFail(event);
                }
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

    private void handleJoinedSuccess(MonopolyEvent event) {
        @SuppressWarnings("unused")
        RespJoinedLobbyEvent e = (RespJoinedLobbyEvent) event;

        EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.Game, new GoLobbyScreenEvent());
    }

    private void handleJoinedFail(MonopolyEvent event) {
        String errorMsg = event.getErrorMsg();

        errorMessageLabel.setText(errorMsg);
    }

}
