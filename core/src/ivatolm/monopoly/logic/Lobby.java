package ivatolm.monopoly.logic;

import java.util.HashMap;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.NetReqUpdateLobbyInfoEvent;
import ivatolm.monopoly.event.events.net.NetRespConnectEvent;

public class Lobby {

    private Game game;
    private GameProperties properties;

    private HashMap<String, Player> players;

    public Lobby(GameProperties properties) {
        this.properties = properties;

        this.players = new HashMap<>();

        this.game = new Game(properties, players);
        this.game.start();

        this.game.setWorking(false);
    }

    public void addPlayer(Player player) {
        MonopolyEvent response = new NetRespConnectEvent();
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

        String uuid = UUID.randomUUID().toString();
        player.setId(uuid);
        players.put(player.getId(), player);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MonopolyEvent updateLobbyInfo = new NetReqUpdateLobbyInfoEvent(getPlayerList());
        broadcast(updateLobbyInfo);

        if (players.size() == properties.getPlayerCount()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            game.setWorking(true);
        }
    }

    public void removePlayer(String uuid) {
        players.remove(uuid);

        MonopolyEvent updateLobbyInfo = new NetReqUpdateLobbyInfoEvent(getPlayerList());
        broadcast(updateLobbyInfo);
    }

    public void removePlayer(Connection connection) {
        this.game.setWorking(false);

        for (Player player : players.values()) {
            if (player.getConnection() == connection) {
                players.remove(player.getId());
                break;
            }
        }

        MonopolyEvent updateLobbyInfo = new NetReqUpdateLobbyInfoEvent(getPlayerList());
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

        game.dispose();
        try {
            game.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
