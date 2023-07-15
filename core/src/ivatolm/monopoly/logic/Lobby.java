package ivatolm.monopoly.logic;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.event.events.net.RespConnectEvent;

public class Lobby {

    private Game game;
    private GameProperties properties;

    private HashMap<String, Player> players;

    public Lobby(GameProperties properties) {
        this.game = new Game(properties);
        this.properties = properties;

        players = new HashMap<>();
    }

    public void addPlayer(Player player) {
        MonopolyEvent response = new RespConnectEvent();
        if (players.size() + 1 > properties.getPlayerCount()) {
            response.setResult(false);
            response.setErrorMsg("Lobby is already full");

            player.getConnection().sendTCP(response);
            player.dispose();
            return;
        } else {
            response.setResult(true);

            player.getConnection().sendTCP(response);
        }

        players.put(player.getUUID(), player);

        MonopolyEvent updateLobbyInfo = new ReqUpdateLobbyInfoEvent(getPlayerList());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        broadcast(updateLobbyInfo);
    }

    public void removePlayer(String uuid) {
        players.remove(uuid);

        MonopolyEvent updateLobbyInfo = new ReqUpdateLobbyInfoEvent(getPlayerList());
        broadcast(updateLobbyInfo);
    }

    public void removePlayer(Connection connection) {
        for (Player player : players.values()) {
            if (player.getConnection() == connection) {
                players.remove(player.getUUID());
                break;
            }
        }

        MonopolyEvent updateLobbyInfo = new ReqUpdateLobbyInfoEvent(getPlayerList());
        broadcast(updateLobbyInfo);
    }

    private void broadcast(MonopolyEvent event) {
        for (Player player : players.values()) {
            player.getConnection().sendTCP(event);
        }
    }

    public Player[] getPlayerList() {
        return players.values().toArray(new Player[0]);
    }

    public void dispose() {
        for (Player player : players.values()) {
            player.dispose();
        }
    }

}
