package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.event.events.request.ReqInitClientEvent;
import ivatolm.monopoly.logic.Player;

public class LobbyScreen extends BaseScreen {

    private VisTable players;

    protected void generateUI() {
        players = new VisTable(true);

        root.add(players);

        stage.addActor(root);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ReqInitClientEvent:
                handleInitClient(event);
                break;
            case ReqUpdateLobbyInfoEvent:
                handleUpdateLobbyInfo(event);
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

    private void handleInitClient(MonopolyEvent event) {
        ReqInitClientEvent e = (ReqInitClientEvent) event;

        e.getClient().addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ReqUpdateLobbyInfoEvent) {
                    ReqUpdateLobbyInfoEvent updateEvent = (ReqUpdateLobbyInfoEvent) object;

                    EventDistributor.send(Endpoint.LobbyScreen, Endpoint.LobbyScreen, updateEvent);
                }
            }
        });
    }

    private void handleUpdateLobbyInfo(MonopolyEvent event) {
        ReqUpdateLobbyInfoEvent e = (ReqUpdateLobbyInfoEvent) event;

        Player[] list = e.getPlayerList();

        players.clear();
        players.add("Connected players count: " + list.length);
        players.row();
        for (Player player : list) {
            players.add(new VisLabel(player.getName()));
            players.row();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
