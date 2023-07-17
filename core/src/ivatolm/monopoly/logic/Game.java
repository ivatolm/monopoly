package ivatolm.monopoly.logic;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqStartGameEvent;

enum GameState {
    Start,
}

public class Game extends Thread {

    private volatile boolean running;
    private volatile boolean working;
    private Lock lock;

    private GameProperties properties;

    private HashMap<String, Player> players;
    private GameState state;

    public Game(GameProperties properties, HashMap<String, Player> players) {
        this.properties = properties;
        this.players = players;

        this.running = true;
        this.working = false;
        this.lock = new ReentrantLock();

        this.state = GameState.Start;
    }

    public GameProperties getProperties() {
        return properties;
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
            if (!working) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            lock.lock();
            update();
            lock.unlock();
        }
    }

    private void update() {
        switch (state) {
            case Start:
                handleStartState();
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
    }

}
