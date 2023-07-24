package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.GameState;

public class NetReqUpdateGameStateEvent extends MonopolyEvent {

    private GameState gameState;

    public NetReqUpdateGameStateEvent() {
        super(Type.NetReqUpdateGameStateEvent);
    }

    public NetReqUpdateGameStateEvent(GameState gameState) {
        super(Type.NetReqUpdateGameStateEvent);

        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

}
