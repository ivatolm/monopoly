package ivatolm.monopoly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.widgets.FlatButtonFactory;

public class MainMenuScreen extends BaseScreen {

    private Stage stage;

    private Table root;

    public MainMenuScreen() {
        super();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        generateUI();
    }

    private void generateUI() {
        root = new Table();
        root.setFillParent(true);

        TextButton button = FlatButtonFactory.FlatButton("Join game", Color.GRAY, 100, 50);
        root.add(button);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
