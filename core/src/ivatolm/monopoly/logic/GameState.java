package ivatolm.monopoly.logic;

import java.util.HashMap;
import java.util.Random;

public class GameState {

    public enum StateType {
        Start,
        TurnStart,
        TurnEnd
    }

    private GameProperties gameProperties;
    private HashMap<String, Player> players;

    private StateType stateType;
    private String[] turnOrder;
    private int turnPtr;

    public GameState() {

    }

    public GameState(GameProperties gameProperties,
            HashMap<String, Player> players,
            StateType startStateType) {
        this.gameProperties = gameProperties;
        this.players = players;

        this.stateType = startStateType;
        this.turnPtr = 0;
        this.turnOrder = new String[] {};
    }

    public void init() {
        updateLobbyData();

        for (Player player : players.values()) {
            player.setMoney(gameProperties.getStartMoneyAmount());
        }
    }

    public void update() {
        updateLobbyData();

        // Updating position of turning player
        Random random = new Random();
        int shift = 2 + random.nextInt(10);

        Player player = players.get(getTurningPlayer());
        int oldPosition = player.getPosition();
        int position = (oldPosition + shift) % 40;
        player.setPosition(position);

        // Give player money for passing START
        // Possible BUG: when going to JAIL accidentally adds money
        if (position < oldPosition) {
            int money = player.getMoney() + gameProperties.getStartMoneyAmount();
            player.setMoney(money);
        }

        turnPtr = (turnPtr + 1) % turnOrder.length;
    }

    public void updateLobbyData() {
        if (players.size() != this.turnOrder.length) {
            this.turnOrder = players.keySet().toArray(new String[0]);
        }
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public StateType getStateType() {
        return stateType;
    }

    public GameProperties getGameProperties() {
        return gameProperties;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public String[] getTurnOrder() {
        return turnOrder;
    }

    public String getTurningPlayer() {
        return turnOrder[turnPtr];
    }

    @Override
    public String toString() {
        String result = "";

        result += "GameState {" + "\n";

        for (Player player : players.values()) {
            result += "  " + player.toString() + "\n";
        }

        result += "  " + "Turn: " + getTurningPlayer() + "\n";

        result += "}" + "\n";

        return result;
    }

}