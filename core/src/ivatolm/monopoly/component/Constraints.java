package ivatolm.monopoly.component;

import lombok.Getter;
import lombok.Setter;

public class Constraints {

    @Getter
    @Setter
    private int x;
    @Getter
    @Setter
    private int y;
    @Getter
    @Setter
    private int width;
    @Getter
    @Setter
    private int height;

    public Constraints() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Constraints(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "(" + "x: " + x + ", " + "y: " + y + ", " + "w: " + width + ", " + "h: " + height + ")";
    }

}
