package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ivatolm.monopoly.component.Constraints;
import ivatolm.monopoly.component.Control;
import ivatolm.monopoly.component.Info;
import ivatolm.monopoly.component.Map;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.game.ReqBuyEvent;
import ivatolm.monopoly.event.events.game.ReqCardSelectedEvent;
import ivatolm.monopoly.event.events.game.ReqPledgeEvent;
import ivatolm.monopoly.event.events.game.ReqSubmitEvent;
import ivatolm.monopoly.event.events.net.NetReqBuyEvent;
import ivatolm.monopoly.event.events.net.NetReqEndGameEvent;
import ivatolm.monopoly.event.events.net.NetReqPledgeEvent;
import ivatolm.monopoly.event.events.net.NetReqSubmitEvent;
import ivatolm.monopoly.event.events.net.NetReqStartGameEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateGameStateEvent;
import ivatolm.monopoly.event.events.request.ReqEndGame;
import ivatolm.monopoly.logic.GameState;
import ivatolm.monopoly.logic.Player;

public class GameScreen extends BaseScreen {

    private Map map;
    private Info info;
    private Control control;

    private Player player;
    private Listener listener;
    private Integer selectedCard;

    private GameState gameState;

    public GameScreen() {
        super();

        Constraints mapConstraints = new Constraints();
        map = new Map(mapConstraints, stage.getCamera());

        Constraints infoConstraints = new Constraints();
        info = new Info(infoConstraints);

        Constraints controlConstraints = new Constraints();
        control = new Control(controlConstraints);
        stage.addActor(control.getRoot());

        listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof NetReqUpdateGameStateEvent) {
                    NetReqUpdateGameStateEvent updateEvent = (NetReqUpdateGameStateEvent) object;

                    EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen, updateEvent);
                }

                else if (object instanceof NetReqEndGameEvent) {
                    NetReqEndGameEvent endGameEvent = (NetReqEndGameEvent) object;

                    EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen, endGameEvent);
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
            map.render(gameState, player);
            info.render(delta);
            // control is rendered with GameScreen stage
        }

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        final int CONTROL_SIZE = 50;

        Constraints mapConstraints = map.getConstraints();
        mapConstraints.setWidth(height);
        mapConstraints.setHeight(height);
        map.setConstraints(mapConstraints);

        Constraints infoConstraints = info.getConstraints();
        infoConstraints.setX(height);
        infoConstraints.setY(CONTROL_SIZE);
        infoConstraints.setWidth(width - height);
        infoConstraints.setHeight(height - CONTROL_SIZE);
        info.setConstraints(infoConstraints);

        Constraints controlConstraints = control.getConstraints();
        controlConstraints.setX(height);
        controlConstraints.setWidth(width - height);
        controlConstraints.setHeight(CONTROL_SIZE);
        control.setConstraints(controlConstraints);

        info.resize(width, height);
        map.resize(width, height);
        control.resize(width, height);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            case NetReqStartGameEvent:
                handleStartGame(event);
                break;
            case NetReqEndGameEvent:
                handleEndGame(event);
                break;
            case NetReqUpdateGameStateEvent:
                handleUpdateGameState(event);
                break;
            case ReqSubmitEvent:
                handleRollDices(event);
                break;
            case ReqBuyEvent:
                handleBuyEvent(event);
                break;
            case ReqPledgeEvent:
                handlePledgeEvent(event);
                break;
            case ReqCardSelectedEvent:
                handleCardSelected(event);
                break;
            default:
                break;
        }
    }

    private void handleStartGame(MonopolyEvent event) {
        NetReqStartGameEvent e = (NetReqStartGameEvent) event;

        player = e.getPlayer();
        player.getConnection().addListener(listener);

        System.out.println("Game has been started.");
    }

    private void handleEndGame(MonopolyEvent event) {
        @SuppressWarnings("unused")
        NetReqEndGameEvent e = (NetReqEndGameEvent) event;

        EventDistributor.send(Endpoint.GameScreen, Endpoint.Game, new ReqEndGame());

        System.out.println("Game has been ended.");
    }

    private void handleUpdateGameState(MonopolyEvent event) {
        NetReqUpdateGameStateEvent e = (NetReqUpdateGameStateEvent) event;

        gameState = e.getGameState();
        info.updatePlayersInfo(gameState);

        if (player.getId().equals(gameState.getTurningPlayerId())) {
            int position = gameState.getPlayers().get(player.getId()).getPosition();
            map.forceUpdateSelection(position);
        }

        System.out.println("Game state was updated");
    }

    private void handleRollDices(MonopolyEvent event) {
        @SuppressWarnings("unused")
        ReqSubmitEvent e = (ReqSubmitEvent) event;

        player.getConnection().sendTCP(new NetReqSubmitEvent());
    }

    private void handleBuyEvent(MonopolyEvent event) {
        @SuppressWarnings("unused")
        ReqBuyEvent e = (ReqBuyEvent) event;

        if (selectedCard != null) {
            player.getConnection().sendTCP(new NetReqBuyEvent(selectedCard));
        }
    }

    private void handlePledgeEvent(MonopolyEvent event) {
        @SuppressWarnings("unused")
        ReqPledgeEvent e = (ReqPledgeEvent) event;

        if (selectedCard != null) {
            player.getConnection().sendTCP(new NetReqPledgeEvent(selectedCard));
        }
    }

    private void handleCardSelected(MonopolyEvent event) {
        ReqCardSelectedEvent e = (ReqCardSelectedEvent) event;

        selectedCard = e.getPosition();
    }

    @Override
    public void dispose() {
        map.dispose();
        info.dispose();
        control.dispose();

        player.getConnection().removeListener(listener);
        player.dispose();
    }

}
