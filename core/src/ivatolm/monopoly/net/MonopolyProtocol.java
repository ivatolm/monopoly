package ivatolm.monopoly.net;

import com.esotericsoftware.kryo.Kryo;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqConnectEvent;
import ivatolm.monopoly.event.events.net.ReqDisconnectEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.logic.Player;

public class MonopolyProtocol {

    public static void setupKryo(Kryo kryo) {
        kryo.register(MonopolyEvent.class);
        kryo.register(MonopolyEvent.Type.class);
        kryo.register(Player.class);
        kryo.register(Player[].class);
        kryo.register(ReqConnectEvent.class);
        kryo.register(ReqDisconnectEvent.class);
        kryo.register(ReqUpdateLobbyInfoEvent.class);
    }

}
