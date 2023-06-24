package ivatolm.monopoly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BaseScreen extends ScreenAdapter {

    protected Viewport viewport;

    public BaseScreen() {
        viewport = new ExtendViewport(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

}
