package ivatolm.monopoly.net;

import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqConnectEvent;
import ivatolm.monopoly.event.events.net.RespConnectEvent;
import ivatolm.monopoly.event.events.net.ReqDisconnectEvent;
import ivatolm.monopoly.event.events.net.ReqStartGameEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateGameStateEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.logic.GameProperties;
import ivatolm.monopoly.logic.GameState;
import ivatolm.monopoly.logic.Player;

public class MonopolyProtocol {

    public static void setupKryo(Kryo kryo) {
        kryo.register(MonopolyEvent.class);
        kryo.register(MonopolyEvent.Type.class);
        kryo.register(Player.class);
        kryo.register(Player[].class);
        kryo.register(ReqConnectEvent.class);
        kryo.register(RespConnectEvent.class);
        kryo.register(ReqDisconnectEvent.class);
        kryo.register(ReqUpdateLobbyInfoEvent.class);
        kryo.register(ReqStartGameEvent.class);
        kryo.register(GameState.class);
        kryo.register(GameState.StateType.class);
        kryo.register(GameProperties.class);
        kryo.register(HashMap.class);
        kryo.register(String[].class);
        kryo.register(ReqUpdateGameStateEvent.class);
    }

}
