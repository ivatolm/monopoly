package ivatolm.monopoly.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.map.Map;

public class GameScreen extends BaseScreen {

    private Map map;

    public GameScreen() {
        super();

        map = new Map(stage.getCamera());
    }

    @Override
    protected void generateUI() {
        stage.addActor(root);
    }

    @Override
    public void handleEvents() {
        MonopolyEvent event = events.pop();
        switch (event.getType()) {
            default:
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.render();
    }

}
