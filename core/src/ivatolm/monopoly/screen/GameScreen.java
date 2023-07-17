package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqStartGameEvent;
import ivatolm.monopoly.logic.Player;
import ivatolm.monopoly.map.Map;

public class GameScreen extends BaseScreen {

    private Map map;

    private Player player;
    private Listener listener;

    public GameScreen() {
        super();

        map = new Map(stage.getCamera());

        listener = new Listener() {
            public void received(Connection connection, Object object) {

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
        map.render();

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

}
