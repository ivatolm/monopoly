package ivatolm.monopoly.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.events.request.CreateLobbyEvent;
import ivatolm.monopoly.widget.FlatWidgetFactory;

public class CreateLobbyScreen extends BaseScreen {

    private TextButton connectButton;
    private Label errorMessageLabel;

    protected void generateUI() {
        connectButton = FlatWidgetFactory.FlatButton("Create", Color.BLUE, 100, 50);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EventDistributor.send(Type.Server, new CreateLobbyEvent());
            }
        });

        errorMessageLabel = FlatWidgetFactory.FlatLabel("");

        VerticalGroup column = new VerticalGroup();
        column.addActor(connectButton);
        column.addActor(errorMessageLabel);

        root.add(column);

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
        ScreenUtils.clear(Color.BLACK);

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
