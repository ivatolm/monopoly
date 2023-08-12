package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisRadioButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.navigation.GoLobbyScreenEvent;
import ivatolm.monopoly.event.events.navigation.GoMainMenuScreenEvent;
import ivatolm.monopoly.event.events.request.ReqConnectToLobbyEvent;
import ivatolm.monopoly.event.events.request.ReqCreateLobbyEvent;
import ivatolm.monopoly.event.events.request.ReqInitClientEvent;
import ivatolm.monopoly.event.events.response.RespLobbyCreatedEvent;
import ivatolm.monopoly.logic.GameProperties;
import ivatolm.monopoly.event.events.response.RespJoinedLobbyEvent;
import ivatolm.monopoly.widget.WidgetConstants;

public class CreateLobbyScreen extends BaseScreen {

    private VisLabel nameLabel;
    private VisTextField nameTextField;
    private ButtonGroup<VisRadioButton> playerCountButtonGroup;
    private VisTextButton connectButton;
    private VisLabel errorMessageLabel;
    private VisTextButton backButton;

    private GameProperties gameProperties;

    public CreateLobbyScreen() {
        gameProperties = new GameProperties();
    }

    protected void generateUI() {
        nameLabel = new VisLabel("Name: ");
        nameTextField = new VisTextField();

        VisRadioButton button2 = new VisRadioButton("2");
        VisRadioButton button3 = new VisRadioButton("3");
        VisRadioButton button4 = new VisRadioButton("4");
        VisRadioButton button5 = new VisRadioButton("5");
        VisRadioButton button6 = new VisRadioButton("6");
        playerCountButtonGroup = new ButtonGroup<>(
                button2, button3, button4, button5, button6);
        playerCountButtonGroup.setMaxCheckCount(1);
        playerCountButtonGroup.setUncheckLast(true);

        connectButton = new VisTextButton("Create");
        errorMessageLabel = new VisLabel("", Color.RED);
        backButton = new VisTextButton("Back");

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameProperties.setPlayerCount(2);
            }
        });
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameProperties.setPlayerCount(3);
            }
        });
        button4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameProperties.setPlayerCount(4);
            }
        });
        button5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameProperties.setPlayerCount(5);
            }
        });
        button6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameProperties.setPlayerCount(6);
            }
        });

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorMessageLabel.setText("");

                String name = nameTextField.getText();
                if (name.isBlank()) {
                    errorMessageLabel.setText("Name cannot be blank");
                    return;
                }

                if (gameProperties.getPlayerCount() == null) {
                    errorMessageLabel.setText("Player count is not chosen");
                    return;
                }

                name = name.strip();

                EventDistributor.send(Endpoint.CreateLobbyScreen, Endpoint.Server,
                        new ReqCreateLobbyEvent(name, gameProperties));
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.Game,
                        new GoMainMenuScreenEvent());
            }
        });

        nameTextField.setFocusBorderEnabled(false);

        button2.setFocusBorderEnabled(false);
        button3.setFocusBorderEnabled(false);
        button4.setFocusBorderEnabled(false);
        button5.setFocusBorderEnabled(false);
        button6.setFocusBorderEnabled(false);

        connectButton.setColor(Color.BLUE);
        connectButton.setFocusBorderEnabled(false);

        backButton.setColor(Color.BLUE);
        backButton.setFocusBorderEnabled(false);

        root.add(nameLabel).colspan(1);
        root.add(nameTextField).colspan(2)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH * 1.5f, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));
        root.row();
        root.add(button2).colspan(1);
        root.add(button3).colspan(1);
        root.add(button4).colspan(1);
        root.row();
        root.add(button5).colspan(1);
        root.add(button6).colspan(1);
        root.row();
        root.add(connectButton).colspan(3)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));
        root.row();
        root.add(errorMessageLabel).colspan(3);
        root.row();
        root.add(backButton).colspan(3)
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
