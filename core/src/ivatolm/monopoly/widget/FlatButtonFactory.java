package ivatolm.monopoly.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import ivatolm.monopoly.resource.ResourceManager;

public class FlatButtonFactory {

    public static TextButton FlatButton(String label, Color color, int width, int height) {
        TextButtonStyle style = new TextButtonStyle();
        style.font = ResourceManager.getFont("OpenSans-Regular", 12);

        Pixmap pm = ResourceManager.getPixmap(width, height);
        pm.setColor(color);
        pm.fill();

        Texture texture = new Texture(pm);
        style.up = new Image(texture).getDrawable();

        TextButton button = new TextButton(label, style);

        return button;
    }

}
