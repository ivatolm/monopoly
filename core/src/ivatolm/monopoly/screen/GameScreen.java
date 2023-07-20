package ivatolm.monopoly.screen;

import java.util.Collection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ivatolm.monopoly.component.Map;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqStartGameEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateGameStateEvent;
import ivatolm.monopoly.logic.GameState;
import ivatolm.monopoly.logic.Player;

public class GameScreen extends BaseScreen {

    private Map map;

    private Player player;
    private Listener listener;

    private GameState gameState;

    public GameScreen() {
        super();

        map = new Map(stage.getCamera());

        listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ReqUpdateGameStateEvent) {
                    ReqUpdateGameStateEvent updateEvent = (ReqUpdateGameStateEvent) object;

                    EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen, updateEvent);
                }
            }
        };
    }

    @Override
    protected void generateUI() {
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        if (gameState != null) {
            Collection<Player> players = gameState.getPlayers().values();
            map.render(players);
        }

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        map.resize(width, height);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case ReqStartGameEvent:
                handleStartGame(event);
                break;
            case ReqUpdateGameStateEvent:
                handleUpdateGameState(event);
                break;
            default:
                break;
        }
    }

    private void handleStartGame(MonopolyEvent event) {
        ReqStartGameEvent e = (ReqStartGameEvent) event;

        player = e.getPlayer();
        player.getConnection().addListener(listener);

        System.out.println("Game has been started.");
    }

    private void handleUpdateGameState(MonopolyEvent event) {
        ReqUpdateGameStateEvent e = (ReqUpdateGameStateEvent) event;

        gameState = e.getGameState();

        System.out.println("Game state was updated");
    }

}
