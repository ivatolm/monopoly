package ivatolm.monopoly.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

import ivatolm.monopoly.resource.ResourceManager;

public class FlatWidgetFactory {

    public static TextButton FlatButton(String text, Color color, int width, int height) {
        TextButtonStyle style = new TextButtonStyle();
        style.font = ResourceManager.getFont("OpenSans-Regular", 16);

        Pixmap pm = ResourceManager.getPixmap(width, height);
        pm.setColor(color);
        pm.fill();

        Texture texture = ResourceManager.getTexture(pm);
        style.up = new Image(texture).getDrawable();

        TextButton button = new TextButton(text, style);

        return button;
    }

    public static TextField FlatTextField(Color color, int width, int height) {
        TextFieldStyle style = new TextFieldStyle();
        style.font = ResourceManager.getFont("OpenSans-Regular", 16);
        style.fontColor = Color.WHITE;

        Pixmap cursorPM = ResourceManager.getPixmap(2, height / 2);
        cursorPM.setColor(Color.WHITE);
        cursorPM.fill();
        Texture cursorTexture = ResourceManager.getTexture(cursorPM);
        style.cursor = new Image(cursorTexture).getDrawable();

        Pixmap backgroundPM = ResourceManager.getPixmap(width, height);
        backgroundPM.setColor(color);
        backgroundPM.fill();

        Texture backgroundTexture = ResourceManager.getTexture(backgroundPM);
        style.background = new Image(backgroundTexture).getDrawable();

        TextField textField = new TextField("", style);

        return textField;
    }

    public static Label FlatLabel(String text) {
        LabelStyle style = new LabelStyle();
        style.fontColor = Color.WHITE;
        style.font = ResourceManager.getFont("OpenSans-Regular", 14);

        Label label = new Label(text, style);

        return label;
    }

}
