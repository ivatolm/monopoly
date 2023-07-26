package ivatolm.monopoly.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
        TurnEnd,
        End
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

    @Getter
    private boolean gameEnded;

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

        this.gameEnded = false;
    }

    public void init() {
        updateLobbyData();

        for (Player player : players.values()) {
            player.setMoney(gameProperties.getStartMoneyAmount());
        }
    }

    public void preUpdate(MonopolyEvent event) {
        Player player = players.get(getTurningPlayerId());
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

        Player player = players.get(getTurningPlayerId());
        int oldPosition = player.getPosition();
        int position = (oldPosition + shift) % 40;
        player.setPosition(position);

        // Give player money for passing START
        // Possible BUG: when going to JAIL accidentally adds money
        if (position < oldPosition) {
            int money = player.getMoney() + gameProperties.getStartMoneyAmount();
            player.setMoney(money);
        }

        // Charging for rent
        Property positionProperty = property.get(position);
        if (positionProperty != null && positionProperty.getOwner() != null
                && !positionProperty.getOwner().equals(player.getId())) {
            int cost = positionProperty.getRentCost();

            String owner = positionProperty.getOwner();
            Player ownerPlayer = players.get(owner);

            tryFindMoney(player, cost);

            player.setMoney(player.getMoney() - cost);
            ownerPlayer.setMoney(ownerPlayer.getMoney() + cost);
        }

        // Checking for getting into jail
        if (position == 30) {
            player.setPosition(10);
        }

        // Checking for getting to pay taxes
        final List<Integer> taxesPositions = Arrays.asList(new Integer[] {
                4, 38
        });
        final int taxesMaxCost = 50;

        if (taxesPositions.contains(position)) {
            int taxesCost = random.nextInt(taxesMaxCost);
            tryFindMoney(player, taxesCost);
            player.setMoney(player.getMoney() - taxesCost);
        }

        // Checking for getting to redeem benefits
        final List<Integer> benefitsPositions = Arrays.asList(new Integer[] {
                2, 17,
        });
        final int benefitsMaxCost = 50;

        if (benefitsPositions.contains(position)) {
            int benefitsCost = random.nextInt(benefitsMaxCost);
            tryFindMoney(player, benefitsCost);
            player.setMoney(player.getMoney() + benefitsCost);
        }

        // Checking for getting any chances
        final List<Integer> chancesPositions = Arrays.asList(new Integer[] {
                7, 22, 36
        });

        if (chancesPositions.contains(position)) {
            int chancesCost = random.nextInt(benefitsMaxCost + taxesMaxCost) - benefitsMaxCost;
            player.setMoney(player.getMoney() + chancesCost);
        }

        // Checking for getting boosts
        final List<Integer> boostPositions = Arrays.asList(new Integer[] {
                5, 12, 15, 25, 28, 35
        });

        if (boostPositions.contains(position)) {
            player.setMoney((int) (player.getMoney() * 1.5f));
        }

        // Checking for game ending
        int bankruptCount = 0;
        for (Player p : players.values()) {
            if (p.isBankrupt()) {
                bankruptCount += 1;
            }
        }

        if (bankruptCount == turnOrder.length - 1) {
            gameEnded = true;
        }

        // Updating turning player
        turnPtr = (turnPtr + 1) % turnOrder.length;
        while (players.get(getTurningPlayerId()).isBankrupt()) {
            turnPtr = (turnPtr + 1) % turnOrder.length;
        }
    }

    public void updateLobbyData() {
        if (players.size() != this.turnOrder.length) {
            this.turnOrder = players.keySet().toArray(new String[0]);
        }
    }

    private void generateProperty() {
        final List<Integer> nonPropertyPositions = Arrays.asList(new Integer[] {
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
            int[] rent = new int[] { 10, 20, 30, 40, 50 };

            Property property = new Property(cost, rent, 0);
            this.property.put(i, property);
        }
    }

    public String getTurningPlayerId() {
        return turnOrder[turnPtr];
    }

    @Override
    public String toString() {
        String result = "";

        result += "GameState {" + "\n";

        for (Player player : players.values()) {
            result += "  " + player.toString() + "\n";
        }

        result += "  " + "Turn: " + getTurningPlayerId() + "\n";

        result += "}" + "\n";

        return result;
    }

    private List<Property> getActivePlayerProperty(String playerId) {
        LinkedList<Property> result = new LinkedList<>();

        for (Property p : property.values()) {
            if (playerId.equals(p.getOwner()) && !p.isPledged()) {
                result.add(p);
            }
        }

        return result;
    }

    private void tryFindMoney(Player player, int cost) {
        Random random = new Random();

        while (player.getMoney() < cost) {
            List<Property> playerProperty = getActivePlayerProperty(player.getId());
            if (playerProperty.isEmpty()) {
                break;
            }

            int propertyIndex = random.nextInt(playerProperty.size());

            Property propertyForPledge = playerProperty.get(propertyIndex);
            player.setMoney(player.getMoney() + propertyForPledge.getPledgedCost());
            propertyForPledge.setPledged(true);
        }
    }

}