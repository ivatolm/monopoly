package ivatolm.monopoly.map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class Map extends ApplicationAdapter {

    private Camera camera;

    private SpriteBatch batch;
    private TextureAtlas cards;
    private Array<Sprite> middleCards;
    private Array<Sprite> cornerCards;

    private float mapHeight;
    private float scalingRatio;

    public Map(Camera camera) {
        this.camera = camera;

        batch = new SpriteBatch();
        loadTextures();
        computeScalingFactor();
    }

    private void loadTextures() {
        middleCards = new Array<>(4);
        cornerCards = new Array<>(4);

        String[] middleCardsNames = {
                "Blue", "Luxury_tax", "Blue", "Orange_chance",
                "Train",
                "Green", "Chest", "Green", "Green",
                "Yellow", "Water_works", "Yellow", "Yellow",
                "Train",
                "Red", "Red", "Blue_chance", "Red",
                "Orange", "Orange", "Chest", "Orange",
                "Train",
                "Pink", "Pink", "Electric_company", "Pink",
                "Skyblue", "Skyblue", "Pink_chance", "Skyblue",
                "Train",
                "Income_tax", "Violet", "Chest", "Violet"
        };
        String[] cornerCardsNames = { "Go", "Police", "Free_parking", "Jail" };

        cards = new TextureAtlas("cards/cards.txt");
        for (String name : middleCardsNames) {
            Sprite sprite = cards.createSprite(name);
            middleCards.add(sprite);
        }

        for (String name : cornerCardsNames) {
            Sprite sprite = cards.createSprite(name);
            cornerCards.add(sprite);
        }
    }

    private void computeScalingFactor() {
        float middleWidth = middleCards.get(0).getWidth();
        float cornerWidth = cornerCards.get(0).getHeight();

        mapHeight = middleWidth * 9 + cornerWidth * 2;
        resize(Gdx.app.getGraphics().getWidth(),
                Gdx.app.getGraphics().getHeight());
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        renderMiddleCards();
        renderCornerCards();

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
                sprite.setOrigin(0, templateHeight);
                sprite.setRotation(90 * row);

                float x = 0, y = 0;
                switch (row) {
                    case 0:
                        x = templateHeight + (i * templateWidth);
                        y = 0;
                        break;
                    case 1:
                        x = templateHeight + (templateHeight + 9 * templateWidth);
                        y = templateHeight + (i * templateWidth);
                        break;
                    case 2:
                        x = templateHeight + 9 * templateWidth - (i * templateWidth);
                        y = templateHeight + (templateHeight + 9 * templateWidth);
                        break;
                    case 3:
                        x = 0;
                        y = templateHeight + 9 * templateWidth - (i * templateWidth);
                        break;
                }

                sprite.setPosition(x, y);

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
        float templateWidth = template.getHeight() * scalingRatio;
        float templateHeight = template.getHeight() * scalingRatio;

        for (int i = 0; i < 4; i++) {
            Sprite sprite = sprites.get(i);
            sprite.setOrigin(0, 0);
            sprite.setRotation(0);

            float x = 0, y = 0;
            switch (i) {
                case 0:
                    x = 0;
                    y = 0;
                    break;
                case 1:
                    x = templateWidth + middleTemplateWidth * 9;
                    y = 0;
                    break;
                case 2:
                    x = templateWidth + middleTemplateWidth * 9;
                    y = templateHeight + middleTemplateWidth * 9;
                    break;
                case 3:
                    x = 0;
                    y = templateHeight + middleTemplateWidth * 9;
                    break;
            }

            sprite.setPosition(x, y);

            sprite.setOrigin(0, 0);
            sprite.setScale(scalingRatio);

            sprite.draw(batch);
        }
    }

    @Override
    public void resize(int width, int height) {
        scalingRatio = height / mapHeight;
    }

    @Override
    public void dispose() {
        batch.dispose();
        cards.dispose();
    }

}
