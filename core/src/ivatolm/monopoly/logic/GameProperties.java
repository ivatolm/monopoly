package ivatolm.monopoly.logic;

import lombok.Getter;

public class GameProperties {

    @Getter
    private int playerCount;
    @Getter
    private int startMoneyAmount = 200;

    public GameProperties() {

    }

    public GameProperties(int playerCount) {
        this.playerCount = playerCount;
    }

}
