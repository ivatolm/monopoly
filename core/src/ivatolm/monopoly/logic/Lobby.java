package ivatolm.monopoly.logic;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.ReqInitPlayerEvent;

public class Lobby {

    private Game game;
    private GameProperties properties;

    private HashMap<UUID, Player> players;

    public Lobby(GameProperties properties) {
        this.game = new Game(properties);
        this.properties = properties;

        players = new HashMap<>();
    }

    public void addPlayer(Player player) {
        if (players.size() + 1 > properties.getPlayerCount()) {
            return;
        }

        players.put(player.getUUID(), player);

        ReqInitPlayerEvent event = new ReqInitPlayerEvent(player);
        try {
            player.getSocket().send(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    private void broadcast(MonopolyEvent event) {
        for (Player player : players.values()) {
            try {
                player.getSocket().send(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<Player> getPlayerList() {
        return players.values();
    }

    public void dispose() {
        for (Player player : players.values()) {
            player.getSocket().dispose();
        }
    }

}
