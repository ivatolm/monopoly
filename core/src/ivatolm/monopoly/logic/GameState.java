package ivatolm.monopoly.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.net.NetReqBuyEvent;
import ivatolm.monopoly.event.events.net.NetReqPledgeEvent;
import lombok.Getter;
import lombok.Setter;

public class GameState {

    public enum StateType {
        Start,
        TurnStart,
        TurnEnd
    }

    @Getter
    private GameProperties gameProperties;
    @Getter
    private HashMap<String, Player> players;
    @Getter
    private HashMap<Integer, Property> property;

    @Getter
    @Setter
    private StateType stateType;
    @Getter
    private String[] turnOrder;
    private int turnPtr;

    public GameState() {

    }

    public GameState(GameProperties gameProperties,
            HashMap<String, Player> players,
            StateType startStateType) {
        this.gameProperties = gameProperties;
        this.players = players;
        this.property = new HashMap<>();
        generateProperty();

        this.stateType = startStateType;
        this.turnPtr = 0;
        this.turnOrder = new String[] {};
    }

    public void init() {
        updateLobbyData();

        for (Player player : players.values()) {
            player.setMoney(gameProperties.getStartMoneyAmount());
        }
    }

    public void preUpdate(MonopolyEvent event) {
        Player player = players.get(getTurningPlayer());
        int playerPosition = player.getPosition();
        int playerMoney = player.getMoney();
        String playerId = player.getId();

        if (event.getType() == MonopolyEvent.Type.NetReqBuyEvent) {
            NetReqBuyEvent buyEvent = (NetReqBuyEvent) event;

            int position = buyEvent.getPosition();
            Property p = property.get(position);

            String owner = p.getOwner();
            Integer cost = p.isPledged() ? p.getPledgedCost() : p.getCost();

            Boolean isOwner = owner == null || owner.equals(playerId);
            Boolean canAfford = playerMoney >= cost;

            if (!p.isPledged() && playerPosition != position) {
                return;
            }

            if (p.canBuy() && isOwner && canAfford) {
                player.setMoney(playerMoney - cost);
                p.setOwner(playerId);

                if (p.isPledged()) {
                    p.setPledged(false);
                } else {
                    p.setStage(p.getStage() + 1);
                }
            }
        }

        else if (event.getType() == MonopolyEvent.Type.NetReqPledgeEvent) {
            NetReqPledgeEvent pledgeEvent = (NetReqPledgeEvent) event;

            int position = pledgeEvent.getPosition();
            Property p = property.get(position);

            String owner = p.getOwner();

            Boolean isOwner = owner == null || owner.equals(playerId);

            if (!p.isPledged() && isOwner) {
                player.setMoney(playerMoney + p.getPledgedCost());
                p.setPledged(true);
            }
        }
    }

    public void update() {
        updateLobbyData();

        // Updating position of turning player
        Random random = new Random();
        int shift = 2 + random.nextInt(10);

        Player player = players.get(getTurningPlayer());
        int oldPosition = player.getPosition();
        int position = (oldPosition + shift) % 40;
        player.setPosition(position);

        // Give player money for passing START
        // Possible BUG: when going to JAIL accidentally adds money
        if (position < oldPosition) {
            int money = player.getMoney() + gameProperties.getStartMoneyAmount();
            player.setMoney(money);
        }

        turnPtr = (turnPtr + 1) % turnOrder.length;
    }

    public void updateLobbyData() {
        if (players.size() != this.turnOrder.length) {
            this.turnOrder = players.keySet().toArray(new String[0]);
        }
    }

    private void generateProperty() {
        List<Integer> nonPropertyPositions = Arrays.asList(new Integer[] {
                0,
                2, 4, 5, 7,
                10,
                12, 15, 17,
                20,
                22, 25, 28,
                30,
                33, 35, 36, 38
        });

        for (int i = 0; i < 40; i++) {
            if (nonPropertyPositions.contains(i)) {
                continue;
            }

            int[] cost = new int[] { 100, 200, 300, 400, 500 };

            Property property = new Property(cost, 0);
            this.property.put(i, property);
        }
    }

    public String getTurningPlayer() {
        return turnOrder[turnPtr];
    }

    @Override
    public String toString() {
        String result = "";

        result += "GameState {" + "\n";

        for (Player player : players.values()) {
            result += "  " + player.toString() + "\n";
        }

        result += "  " + "Turn: " + getTurningPlayer() + "\n";

        result += "}" + "\n";

        return result;
    }

}