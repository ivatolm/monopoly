package ivatolm.monopoly.logic;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqStartGameEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateGameStateEvent;

public class Game extends Thread {

    private volatile boolean running;
    private volatile boolean working;
    private Lock lock;

    private HashMap<String, Player> players;

    private GameState state;

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
            state.update();
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
                break;

            default:
                break;
        }
    }

    private void handleStartState() {
        for (Player p : players.values()) {
            MonopolyEvent startGameEvent = new ReqStartGameEvent(p);
            p.getConnection().sendTCP(startGameEvent);
        }

        state.setStateType(GameState.StateType.TurnStart);
    }

    private void handleTurnStartState() {
        for (Player p : players.values()) {
            p.setPosition(0);
            p.setMoney(200);
        }

        for (Player p : players.values()) {
            MonopolyEvent updateGameStateEvent = new ReqUpdateGameStateEvent(state);
            p.getConnection().sendTCP(updateGameStateEvent);
        }

        state.setStateType(GameState.StateType.TurnEnd);
    }

}
