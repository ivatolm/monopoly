package ivatolm.monopoly.logic;

import java.util.HashMap;

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

    GameState() {

    }

    GameState(GameProperties gameProperties,
            HashMap<String, Player> players,
            StateType startStateType) {
        this.gameProperties = gameProperties;
        this.players = players;

        this.stateType = startStateType;
        this.turnPtr = 0;

        update();
    }

    public void update() {
        this.turnOrder = players.keySet().toArray(new String[0]);
    }

    public String nextTurning() {
        String result = turnOrder[turnPtr];
        turnPtr = (turnPtr++) % turnOrder.length;
        return result;
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

}