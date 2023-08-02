package ivatolm.monopoly.net;

import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.NetReqBuyEvent;
import ivatolm.monopoly.event.events.net.NetReqConnectEvent;
import ivatolm.monopoly.event.events.net.NetRespConnectEvent;
import ivatolm.monopoly.event.events.net.NetReqDisconnectEvent;
import ivatolm.monopoly.event.events.net.NetReqEndGameEvent;
import ivatolm.monopoly.event.events.net.NetReqPledgeEvent;
import ivatolm.monopoly.event.events.net.NetReqSubmitEvent;
import ivatolm.monopoly.event.events.net.NetReqStartGameEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateGameStateEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.logic.GameProperties;
import ivatolm.monopoly.logic.GameState;
import ivatolm.monopoly.logic.Player;
import ivatolm.monopoly.logic.Property;

public class MonopolyProtocol {

    public static void setupKryo(Kryo kryo) {
        kryo.register(MonopolyEvent.class);
        kryo.register(MonopolyEvent.Type.class);
        kryo.register(Player.class);
        kryo.register(Player[].class);
        kryo.register(NetReqConnectEvent.class);
        kryo.register(NetRespConnectEvent.class);
        kryo.register(NetReqDisconnectEvent.class);
        kryo.register(NetReqUpdateLobbyInfoEvent.class);
        kryo.register(NetReqStartGameEvent.class);
        kryo.register(GameState.class);
        kryo.register(GameState.StateType.class);
        kryo.register(GameProperties.class);
        kryo.register(HashMap.class);
        kryo.register(String[].class);
        kryo.register(NetReqUpdateGameStateEvent.class);
        kryo.register(NetReqSubmitEvent.class);
        kryo.register(NetReqBuyEvent.class);
        kryo.register(NetReqPledgeEvent.class);
        kryo.register(int[].class);
        kryo.register(Property.class);
        kryo.register(NetReqEndGameEvent.class);
    }

}
