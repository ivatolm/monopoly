package ivatolm.monopoly.map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map extends ApplicationAdapter {

    Camera camera;

    SpriteBatch batch;

    Texture middleCardTexture;
    Texture cornerCardTexture;

    public Map(Camera camera) {
        this.camera = camera;

        batch = new SpriteBatch();
        createTextures();
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(cornerCardTexture, 0, 0);
        batch.draw(middleCardTexture, 100, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        middleCardTexture.dispose();
        cornerCardTexture.dispose();
    }

    private void createTextures() {
        Pixmap rawPM = new Pixmap(Gdx.files.internal("card.jpg"));

        Pixmap middlePM = new Pixmap(75, 100, rawPM.getFormat());
        middlePM.drawPixmap(rawPM,
                0, 0, rawPM.getWidth(), rawPM.getHeight(),
                0, 0, middlePM.getWidth(), middlePM.getHeight());

        Pixmap cornerPM = new Pixmap(100, 100, rawPM.getFormat());
        cornerPM.drawPixmap(rawPM,
                0, 0, rawPM.getWidth(), rawPM.getHeight(),
                0, 0, cornerPM.getWidth(), cornerPM.getHeight());

        middleCardTexture = new Texture(middlePM);
        cornerCardTexture = new Texture(cornerPM);

        rawPM.dispose();
        middlePM.dispose();
        cornerPM.dispose();
    }

}
