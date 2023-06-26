package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.navigation.GoLobbyScreenEvent;
import ivatolm.monopoly.event.events.request.ConnectLobbyEvent;
import ivatolm.monopoly.event.events.response.JoinedLobbyEvent;
import ivatolm.monopoly.widget.FlatWidgetFactory;

public class JoinLobbyScreen extends BaseScreen {

    private TextField ipTextField;
    private TextButton connectButton;
    private Label errorMessageLabel;

    protected void generateUI() {
        ipTextField = FlatWidgetFactory.FlatTextField(Color.GRAY, 100, 50);

        connectButton = FlatWidgetFactory.FlatButton("Connect", Color.BLUE, 100, 50);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorMessageLabel.setText("");
                EventDistributor.send(Type.JoinLobbyScreen, Type.Client, new ConnectLobbyEvent(ipTextField.getText()));
            }
        });

        errorMessageLabel = FlatWidgetFactory.FlatLabel("");

        VerticalGroup column = new VerticalGroup();
        column.addActor(ipTextField);
        column.addActor(connectButton);
        column.addActor(errorMessageLabel);

        root.add(column);

        stage.addActor(root);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        System.out.println(event);
        switch (event.getType()) {
            case JoinedLobby:
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
        JoinedLobbyEvent e = (JoinedLobbyEvent) event;

        EventDistributor.send(Type.JoinLobbyScreen, Type.Game, new GoLobbyScreenEvent());
    }

    private void handleJoinedFail(MonopolyEvent event) {
        String errorMsg = event.getErrorMsg();

        errorMessageLabel.setText(errorMsg);
    }

}
