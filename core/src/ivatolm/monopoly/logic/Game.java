package ivatolm.monopoly.logic;

import java.util.HashMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.NetReqBuyEvent;
import ivatolm.monopoly.event.events.net.NetReqEndGameEvent;
import ivatolm.monopoly.event.events.net.NetReqPledgeEvent;
import ivatolm.monopoly.event.events.net.NetReqSubmitEvent;
import ivatolm.monopoly.event.events.net.NetReqStartGameEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateGameStateEvent;

public class Game extends Thread {

    private volatile boolean running;
    private volatile boolean working;

    private HashMap<String, Player> players;

    private GameState state;

    private volatile boolean waitingResponse = false;

    public Game(GameProperties properties, HashMap<String, Player> players) {
        this.players = players;
        this.state = new GameState(properties, players, GameState.StateType.Start);

        this.running = true;
        this.working = false;
    }

    public GameProperties getProperties() {
        return state.getGameProperties();
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public void dispose() {
        this.working = false;
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!working) {
                continue;
            }

            update();
        }
    }

    private void update() {
        switch (state.getStateType()) {
            case Start:
                handleStartState();
                break;

            case TurnStart:
                handleTurnStartState();
                break;

            case TurnEnd:
                handleTurnEndState();
                break;

            case End:
                handleEndState();
                break;

            default:
                break;
        }
    }

    private void handleStartState() {
        for (Player p : players.values()) {
            MonopolyEvent startGameEvent = new NetReqStartGameEvent(p);
            p.getConnection().sendTCP(startGameEvent);
        }

        state.init();
        state.setStateType(GameState.StateType.TurnStart);
    }

    private void handleTurnStartState() {
        state.update();

        sendGameState();

        state.setStateType(GameState.StateType.TurnEnd);
    }

    private void handleTurnEndState() {
        Player player = players.get(state.getTurningPlayerId());

        waitingResponse = true;

        Listener listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof NetReqBuyEvent) {
                    NetReqBuyEvent buyEvent = (NetReqBuyEvent) object;

                    state.process(buyEvent);
                    sendGameState();
                }

                else if (object instanceof NetReqPledgeEvent) {
                    NetReqPledgeEvent plegdeEvent = (NetReqPledgeEvent) object;

                    state.process(plegdeEvent);
                    sendGameState();
                }

                else if (object instanceof NetReqSubmitEvent) {
                    @SuppressWarnings("unused")
                    NetReqSubmitEvent submitEvent = (NetReqSubmitEvent) object;

                    waitingResponse = false;
                }
            }
        };

        player.getConnection().addListener(listener);
        while (running && waitingResponse) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        player.getConnection().removeListener(listener);

        state.setStateType(GameState.StateType.TurnStart);
    }

    private void handleEndState() {
        System.out.println("Game finished");

        for (Player p : players.values()) {
            MonopolyEvent endGameEvent = new NetReqEndGameEvent();
            p.getConnection().sendTCP(endGameEvent);
        }
    }

    private void sendGameState() {
        for (Player p : players.values()) {
            MonopolyEvent updateGameStateEvent = new NetReqUpdateGameStateEvent(state);
            p.getConnection().sendTCP(updateGameStateEvent);
        }
    }

}
