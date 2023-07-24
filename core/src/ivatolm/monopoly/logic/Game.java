package ivatolm.monopoly.logic;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.NetReqRollDicesEvent;
import ivatolm.monopoly.event.events.net.NetReqStartGameEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateGameStateEvent;

public class Game extends Thread {

    private volatile boolean running;
    private volatile boolean working;
    private Lock lock;

    private HashMap<String, Player> players;

    private GameState state;

    private volatile boolean waitingResponse = false;

    private final int START_MONEY_AMOUNT = 200;

    public Game(GameProperties properties, HashMap<String, Player> players) {
        this.players = players;
        this.state = new GameState(properties, players, GameState.StateType.Start);

        this.running = true;
        this.working = false;
        this.lock = new ReentrantLock();
    }

    public GameProperties getProperties() {
        return state.getGameProperties();
    }

    public void setWorking(boolean working) {
        lock.lock();
        this.working = working;
        lock.unlock();
    }

    public void dispose() {
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

            lock.lock();
            update();
            lock.unlock();
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
        for (Player p : players.values()) {
            MonopolyEvent updateGameStateEvent = new NetReqUpdateGameStateEvent(state);
            p.getConnection().sendTCP(updateGameStateEvent);
        }

        state.setStateType(GameState.StateType.TurnEnd);
    }

    private void handleTurnEndState() {
        Player player = players.get(state.getTurningPlayer());

        waitingResponse = true;

        Listener listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof NetReqRollDicesEvent) {
                    @SuppressWarnings("unused")
                    NetReqRollDicesEvent rollDicesEvent = (NetReqRollDicesEvent) object;

                    waitingResponse = false;
                    state.update();

                    System.out.println("Updating game state...");
                }
            }
        };

        player.getConnection().addListener(listener);
        while (waitingResponse) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        player.getConnection().removeListener(listener);

        state.setStateType(GameState.StateType.TurnStart);
    }

}
