package ivatolm.monopoly.screen;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisTable;

import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;

public abstract class BaseScreen extends ScreenAdapter
        implements EventReceiver {

    protected LinkedList<MonopolyEvent> events;
    protected Stage stage;
    protected VisTable root;

    public BaseScreen() {
        events = new LinkedList<>();
        Viewport viewport = new ExtendViewport(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        root = new VisTable(true);
        root.setFillParent(true);
        generateUI();
    }

    public void setAsInputHandler() {
        Gdx.input.setInputProcessor(stage);
    }

    protected abstract void generateUI();

    @Override
    public void receive(MonopolyEvent event) {
        events.add(event);
    }

    @Override
    public abstract void handleEvents();

    @Override
    public void render(float delta) {
        if (events.size() > 0) {
            handleEvents();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
