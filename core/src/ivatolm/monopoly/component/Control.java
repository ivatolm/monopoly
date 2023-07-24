package ivatolm.monopoly.component;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver.Endpoint;
import ivatolm.monopoly.event.events.game.ReqBuyEvent;
import ivatolm.monopoly.event.events.game.ReqPledgeEvent;
import ivatolm.monopoly.event.events.game.ReqRollDicesEvent;
import lombok.Getter;
import lombok.Setter;

public class Control extends ApplicationAdapter {

    private VisTable root;

    @Getter
    @Setter
    private Constraints constraints;

    public Control(Constraints constraints) {
        this.constraints = constraints;

        this.root = new VisTable(true);

        generateUI();
    }

    public void generateUI() {
        this.root.left().top();

        VisTextButton rollDicesButton = new VisTextButton("Roll dices");
        VisTextButton buyButton = new VisTextButton("Buy");
        VisTextButton pledgeButton = new VisTextButton("Pledge");

        rollDicesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen, new ReqRollDicesEvent());
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen, new ReqBuyEvent());
            }
        });

        pledgeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen, new ReqPledgeEvent());
            }
        });

        rollDicesButton.setFocusBorderEnabled(false);
        buyButton.setFocusBorderEnabled(false);
        pledgeButton.setFocusBorderEnabled(false);

        root.add(rollDicesButton);
        root.add(buyButton);
        root.add(pledgeButton);
    }

    public Table getRoot() {
        return root;
    }

    @Override
    public void resize(int width, int height) {
        int x = constraints.getX();
        int y = constraints.getY();
        int w = constraints.getWidth();
        int h = constraints.getHeight();

        root.setX(x + 10);
        root.setY(y);
        root.setWidth(w - 10);
        root.setHeight(h);
    }

}
