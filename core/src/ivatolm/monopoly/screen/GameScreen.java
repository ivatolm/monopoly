package ivatolm.monopoly.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import ivatolm.monopoly.map.Map;

public class GameScreen extends ScreenAdapter {

    OrthographicCamera camera;
    ExtendViewport viewport;

    private Map map;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(
                Gdx.app.getGraphics().getWidth(),
                Gdx.app.getGraphics().getHeight(),
                camera);
        map = new Map(camera);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

}
