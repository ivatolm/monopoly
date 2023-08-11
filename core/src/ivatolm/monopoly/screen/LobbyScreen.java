package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.navigation.GoGameScreenEvent;
import ivatolm.monopoly.event.events.navigation.GoMainMenuScreenEvent;
import ivatolm.monopoly.event.events.net.NetReqStartGameEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.event.events.request.ReqInitClientEvent;
import ivatolm.monopoly.logic.Player;
import ivatolm.monopoly.widget.WidgetConstants;

public class LobbyScreen extends BaseScreen {

    private VisTable players;
    private VisTextButton backButton;

    private Client client;
    private Listener listener;

    public LobbyScreen() {
        super();

        listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof NetReqUpdateLobbyInfoEvent) {
                    NetReqUpdateLobbyInfoEvent updateEvent = (NetReqUpdateLobbyInfoEvent) object;

                    EventDistributor.send(Endpoint.LobbyScreen, Endpoint.LobbyScreen, updateEvent);
                }

                if (object instanceof NetReqStartGameEvent) {
                    NetReqStartGameEvent startEvent = (NetReqStartGameEvent) object;

                    EventDistributor.send(Endpoint.LobbyScreen, Endpoint.LobbyScreen, startEvent);
                }
            }
        };
    }

    protected void generateUI() {
        players = new VisTable(true);

        backButton = new VisTextButton("Back");

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.JoinLobbyScreen, Endpoint.Game,
                        new GoMainMenuScreenEvent());
            }
        });

        backButton.setColor(Color.BLUE);
        backButton.setFocusBorderEnabled(false);

        root.add(players);
        root.row();
        root.add(backButton).colspan(2)
                .width(Value.percentWidth(WidgetConstants.BUTTON_WIDTH, root))
                .height(Value.percentHeight(WidgetConstants.BUTTON_HEIGHT, root));

        stage.addActor(root);
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

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ReqInitClientEvent:
                handleInitClient(event);
                break;
            case NetReqUpdateLobbyInfoEvent:
                handleUpdateLobbyInfo(event);
                break;
            case NetReqStartGameEvent:
                handleStartGameEvent(event);
                break;
            default:
                break;
        }
    }

    private void handleInitClient(MonopolyEvent event) {
        ReqInitClientEvent e = (ReqInitClientEvent) event;

        client = e.getClient();

        client.addListener(listener);
    }

    private void handleUpdateLobbyInfo(MonopolyEvent event) {
        NetReqUpdateLobbyInfoEvent e = (NetReqUpdateLobbyInfoEvent) event;

        Player[] list = e.getPlayerList();

        players.clear();
        players.add("Connected players count: " + list.length);
        players.row();
        for (Player player : list) {
            players.add(new VisLabel(player.getName()));
            players.row();
        }
    }

    private void handleStartGameEvent(MonopolyEvent event) {
        NetReqStartGameEvent e = (NetReqStartGameEvent) event;
        e.getPlayer().setConnection(client);

        client.removeListener(listener);

        EventDistributor.send(Endpoint.LobbyScreen, Endpoint.Game, new GoGameScreenEvent());
        EventDistributor.send(Endpoint.LobbyScreen, Endpoint.GameScreen, e);
    }

}
