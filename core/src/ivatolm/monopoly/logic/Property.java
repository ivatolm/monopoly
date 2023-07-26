package ivatolm.monopoly.logic;

import lombok.Getter;
import lombok.Setter;

public class Property {

    private int[] cost;

    private int[] rent;

    @Getter
    @Setter
    private int stage;

    @Getter
    @Setter
    private String owner;

    @Getter
    @Setter
    private boolean pledged;

    public Property() {

    }

    public Property(int[] cost, int[] rent, int stage) {
        this.cost = cost;
        this.rent = rent;
        this.stage = stage;
        this.pledged = false;
    }

    public int getCost() {
        return cost[stage];
    }

    public int getPledgedCost() {
        if (!pledged) {
            return cost[0] / 2;
        }

        return cost[stage - 1] / 2;
    }

    public int getRentCost() {
        return rent[stage];
    }

    public boolean canBuy() {
        return stage < cost.length - 1;
    }

}
