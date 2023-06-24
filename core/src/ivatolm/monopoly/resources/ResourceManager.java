package ivatolm.monopoly.resources;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class ResourceManager {

    private static HashMap<String, BitmapFont> fonts = new HashMap<>();
    private static LinkedList<Pixmap> pixmaps = new LinkedList<>();
    private static LinkedList<Texture> textures = new LinkedList<>();

    public static BitmapFont getFont(String name, int size) {
        BitmapFont font = fonts.get(name + size);
        if (font == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                    Gdx.files.internal("fonts/" + name + ".ttf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = size;

            font = generator.generateFont(parameter);

            fonts.put(name, font);
            generator.dispose();
        }

        return font;
    }

    public static Pixmap getPixmap(int width, int height) {
        Pixmap pm = new Pixmap(width, height, Pixmap.Format.RGB888);
        pixmaps.add(pm);

        return pm;
    }

    public static Texture getTexture(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        textures.add(texture);

        return texture;
    }

    public static void dispose() {
        for (BitmapFont item : fonts.values()) {
            item.dispose();
        }

        for (Pixmap item : pixmaps) {
            item.dispose();
        }

        for (Texture item : textures) {
            item.dispose();
        }
    }

}
