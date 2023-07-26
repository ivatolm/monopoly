package ivatolm.monopoly.component;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import ivatolm.monopoly.logic.GameState;
import ivatolm.monopoly.logic.Player;
import lombok.Getter;
import lombok.Setter;

public class Info extends ApplicationAdapter {

    private Stage stage;
    private VisTable root;

    @Getter
    @Setter
    private Constraints constraints;

    public Info(Constraints constraints) {
        this.constraints = constraints;

        Viewport viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.root = new VisTable(true);
        this.root.left().top();
        stage.addActor(root);
    }

    public void updatePlayersInfo(GameState gameState) {
        root.clear();

        root.add(new VisLabel("Players info:"));
        root.row();
        root.add(new VisLabel("Name"));
        root.add(new VisLabel("Money"));
        root.add(new VisLabel("Turning"));
        root.row();

        for (Player player : gameState.getPlayers().values()) {
            String name = player.getName();
            String money = String.valueOf(player.getMoney());
            Boolean turning = gameState.getTurningPlayerId().equals(player.getId());

            VisLabel nameLabel = new VisLabel(name);
            root.add(nameLabel);
            VisLabel moneyLabel = new VisLabel(money + "$");
            root.add(moneyLabel);
            VisLabel turningLabel = new VisLabel(turning.toString());
            root.add(turningLabel);

            root.row();
        }
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        int x = constraints.getX();
        int y = constraints.getY();
        int w = constraints.getWidth();
        int h = constraints.getHeight();

        root.setX(x + 10);
        root.setY(y);
        root.setWidth(w - 10);
        root.setHeight(h);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
