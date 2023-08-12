package ivatolm.monopoly.logic;

import lombok.Getter;
import lombok.Setter;

public class GameProperties {

    @Getter
    @Setter
    private Integer playerCount = null;
    @Getter
    private int startMoneyAmount = 200;

    public GameProperties() {

    }

}
