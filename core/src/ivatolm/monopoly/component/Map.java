package ivatolm.monopoly.component;

import java.util.Collection;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;

import ivatolm.monopoly.logic.Player;

public class Map extends ApplicationAdapter {

    private Stage stage;
    private VisTable root;

    private Camera camera;

    private SpriteBatch batch;

    private TextureAtlas cards;
    private Array<Sprite> middleCards;
    private Array<Sprite> cornerCards;
    private Array<Sprite> allCards;

    private Texture playerIconTexture;

    private float mapHeight;
    private float scalingRatio;

    public Map(Camera camera) {
        this.stage = new Stage();
        this.root = new VisTable(true);
        stage.addActor(root);

        this.camera = camera;

        batch = new SpriteBatch();

        loadTextures();
        computeScalingFactor();
        generatePlayerIcon();
    }

    @Override
    public void resize(int width, int height) {
        scalingRatio = height / mapHeight;

        if (playerIconTexture != null) {
            playerIconTexture.dispose();
        }

        generatePlayerIcon();
    }

    @Override
    public void dispose() {
        batch.dispose();
        cards.dispose();

        playerIconTexture.dispose();
    }

    private void loadTextures() {
        final String[] middleCardsNames = {
                "Violet", "Chest", "Violet", "Income_tax",
                "Train",
                "Skyblue", "Pink_chance", "Skyblue", "Skyblue",

                "Pink", "Electric_company", "Pink", "Pink",
                "Train",
                "Orange", "Chest", "Orange", "Orange",

                "Red", "Blue_chance", "Red", "Red",
                "Train",
                "Yellow", "Yellow", "Water_works", "Yellow",

                "Green", "Green", "Chest", "Green",
                "Train",
                "Orange_chance", "Blue", "Luxury_tax", "Blue"
        };
        final String[] cornerCardsNames = { "Go", "Jail", "Free_parking", "Police" };

        middleCards = new Array<>(middleCardsNames.length);
        cornerCards = new Array<>(cornerCardsNames.length);

        cards = new TextureAtlas("cards/cards.txt");
        for (String name : middleCardsNames) {
            Sprite sprite = cards.createSprite(name);
            middleCards.add(sprite);
        }

        for (String name : cornerCardsNames) {
            Sprite sprite = cards.createSprite(name);
            cornerCards.add(sprite);
        }

        allCards = new Array<>(middleCardsNames.length + cornerCardsNames.length);
        for (int row = 0; row < 4; row++) {
            allCards.add(cornerCards.get(row));
            for (int i = 0; i < 9; i++) {
                allCards.add(middleCards.get(row * 9 + i));
            }
        }
    }

    private void generatePlayerIcon() {
        final int ICON_SIZE = (int) (50 * scalingRatio);

        Pixmap pixmap = new Pixmap(ICON_SIZE, ICON_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle(ICON_SIZE / 2, ICON_SIZE / 2, ICON_SIZE / 2);
        playerIconTexture = new Texture(pixmap, true);

        pixmap.dispose();
    }

    private void computeScalingFactor() {
        float middleWidth = middleCards.get(0).getWidth();
        float cornerWidth = cornerCards.get(0).getHeight();

        mapHeight = middleWidth * 9 + cornerWidth * 2;
        resize(Gdx.app.getGraphics().getWidth(),
                Gdx.app.getGraphics().getHeight());
    }

    public void render(Collection<Player> players) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        renderMiddleCards();
        renderCornerCards();
        renderPlayers(players);

        batch.end();
    }

    private void renderMiddleCards() {
        Array<Sprite> sprites = middleCards;

        Sprite template = sprites.get(0);
        float templateWidth = template.getWidth() * scalingRatio;
        float templateHeight = template.getHeight() * scalingRatio;
        for (int row = 0; row < 4; row++) {
            for (int i = 0; i < 9; i++) {
                Sprite sprite = sprites.get(row * 9 + i);

                float x = 0, y = 0;
                switch (row) {
                    case 0:
                        x = 0;
                        y = templateHeight + (templateWidth + i * templateWidth);
                        break;
                    case 1:
                        x = templateHeight + (templateWidth + i * templateWidth);
                        y = templateHeight + (templateHeight + 9 * templateWidth);
                        break;
                    case 2:
                        x = templateHeight + (templateHeight + 9 * templateWidth);
                        y = templateHeight + (9 * templateWidth - templateWidth - i * templateWidth);
                        break;
                    case 3:
                        x = templateHeight + (9 * templateWidth - templateWidth - i * templateWidth);
                        y = 0;
                        break;
                }

                sprite.setPosition(x, y);

                sprite.setOrigin(0, templateHeight);
                sprite.setRotation(270 - 90 * row);
                sprite.setOrigin(0, 0);

                sprite.setScale(scalingRatio);

                sprite.draw(batch);
            }
        }
    }

    private void renderCornerCards() {
        Array<Sprite> sprites = cornerCards;

        Sprite middleTemplate = middleCards.get(0);
        float middleTemplateWidth = middleTemplate.getWidth() * scalingRatio;

        Sprite template = sprites.get(0);
        float templateWidth = template.getWidth() * scalingRatio;
        float templateHeight = template.getHeight() * scalingRatio;

        for (int i = 0; i < 4; i++) {
            Sprite sprite = sprites.get(i);

            float x = 0, y = 0;
            switch (i) {
                case 0:
                    x = 0;
                    y = 0;
                    break;
                case 1:
                    x = 0;
                    y = templateHeight + middleTemplateWidth * 9;

                    break;
                case 2:
                    x = templateWidth + middleTemplateWidth * 9;
                    y = templateHeight + middleTemplateWidth * 9;
                    break;
                case 3:
                    x = templateWidth + middleTemplateWidth * 9;
                    y = 0;
                    break;
            }

            sprite.setPosition(x, y);

            sprite.setOrigin(0, 0);
            sprite.setRotation(0);
            sprite.setOrigin(0, 0);
            sprite.setScale(scalingRatio);

            sprite.draw(batch);
        }
    }

    private void renderPlayers(Collection<Player> players) {
        for (Player player : players) {
            int position = player.getPosition();
            Sprite card = allCards.get(position);

            Rectangle bounds = card.getBoundingRectangle();

            float centerX = bounds.x + bounds.width / 2;
            float centerY = bounds.y + bounds.height / 2;

            batch.draw(playerIconTexture,
                    centerX - playerIconTexture.getWidth() / 2,
                    centerY - playerIconTexture.getHeight() / 2);
        }
    }

}
