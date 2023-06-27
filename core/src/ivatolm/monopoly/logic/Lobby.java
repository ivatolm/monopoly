package ivatolm.monopoly.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Lobby {

    private Game game;
    private GameProperties properties;

    private HashMap<UUID, Player> players;

    public Lobby(GameProperties properties) {
        this.game = new Game(properties);
        this.properties = properties;

        players = new HashMap<>();
    }

    public boolean addPlayer(Player player) {
        if (players.size() + 1 <= properties.getPlayerCount()) {
            players.put(player.getUUID(), player);
            return true;
        }

        return false;
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
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
