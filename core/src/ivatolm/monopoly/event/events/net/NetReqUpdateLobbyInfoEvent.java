package ivatolm.monopoly.event.events.net;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.logic.Player;
import lombok.Getter;

public class NetReqUpdateLobbyInfoEvent extends MonopolyEvent {

    @Getter
    private Player[] players;
    @Getter
    private int requiredPlayersCount;

    public NetReqUpdateLobbyInfoEvent() {
        super(Type.NetReqUpdateLobbyInfoEvent);
    }

    public NetReqUpdateLobbyInfoEvent(Player[] players, int requiredPlayersCount) {
        super(Type.NetReqUpdateLobbyInfoEvent);

        this.requiredPlayersCount = requiredPlayersCount;
        this.players = players;
    }

}
