package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.GameState;

public class ReqUpdateGameStateEvent extends MonopolyEvent {

    private GameState gameState;

    public ReqUpdateGameStateEvent() {
        super(Type.ReqUpdateGameStateEvent);
    }

    public ReqUpdateGameStateEvent(GameState gameState) {
        super(Type.ReqUpdateGameStateEvent);

        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

}
