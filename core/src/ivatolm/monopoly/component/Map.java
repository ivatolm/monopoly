package ivatolm.monopoly.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;

import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.event.EventReceiver.Endpoint;
import ivatolm.monopoly.event.events.game.ReqCardSelectedEvent;
import ivatolm.monopoly.logic.GameState;
import ivatolm.monopoly.logic.Player;
import ivatolm.monopoly.logic.Property;
import lombok.Getter;
import lombok.Setter;

public class Map extends ApplicationAdapter {

    private Stage stage;
    private VisTable root;

    Thread inputThread;

    @Getter
    @Setter
    private Constraints constraints;
    private Camera camera;

    private SpriteBatch batch;

    private TextureAtlas cards;
    private Array<Sprite> middleCards;
    private Array<Sprite> cornerCards;
    private Array<Sprite> allCards;

    private Array<Texture> playerIconTextures;
    private Texture thisPlayerIconTexture;
    private BitmapFont font;

    private float mapHeight;
    private float scalingRatio;
    private Sprite selectedCard;

    private final int[] nonPropertyPositions = {
            0,
            2, 4, 5, 7,
            10,
            12, 15, 17,
            20,
            22, 25, 28,
            30,
            33, 35, 36, 38
    };

    public Map(Constraints constraints, Camera camera) {
        this.stage = new Stage();
        this.root = new VisTable(true);
        stage.addActor(root);

        this.constraints = constraints;
        this.camera = camera;

        this.batch = new SpriteBatch();

        loadTextures();
        computeScalingFactor();
        generatePlayerIcon();

        this.font = new BitmapFont();
        font.setColor(Color.GREEN);
    }

    @Override
    public void resize(int width, int height) {
        scalingRatio = height / mapHeight;

        if (playerIconTextures != null) {
            for (Texture texture : playerIconTextures) {
                texture.dispose();
            }

            playerIconTextures = null;
        }

        if (thisPlayerIconTexture != null) {
            thisPlayerIconTexture.dispose();
        }

        generatePlayerIcon();
    }

    @Override
    public void dispose() {
        batch.dispose();
        cards.dispose();

        for (Texture texture : playerIconTextures) {
            texture.dispose();
        }

        thisPlayerIconTexture.dispose();
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

        Color[] colors = {
                Color.MAGENTA, Color.BLUE, Color.GREEN,
                Color.BROWN, Color.CYAN, Color.YELLOW
        };

        playerIconTextures = new Array<>();
        for (Color color : colors) {
            Pixmap pixmap = new Pixmap(ICON_SIZE, ICON_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fillCircle(ICON_SIZE / 2, ICON_SIZE / 2, ICON_SIZE / 2);

            playerIconTextures.add(new Texture(pixmap, true));

            pixmap.dispose();
        }

        Pixmap pixmap = new Pixmap(ICON_SIZE, ICON_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle(ICON_SIZE / 3, ICON_SIZE / 3, ICON_SIZE / 3);

        thisPlayerIconTexture = new Texture(pixmap, true);

        pixmap.dispose();
    }

    private void computeScalingFactor() {
        float middleWidth = middleCards.get(0).getWidth();
        float cornerWidth = cornerCards.get(0).getHeight();

        mapHeight = middleWidth * 9 + cornerWidth * 2;
        resize(Gdx.app.getGraphics().getWidth(),
                Gdx.app.getGraphics().getHeight());
    }

    public void forceUpdateSelection(Integer position) {
        selectedCard = allCards.get(position);

        boolean restriced = false;
        for (int i = 0; i < nonPropertyPositions.length; i++) {
            if (nonPropertyPositions[i] == position) {
                restriced = true;
                break;
            }
        }

        if (position == null || restriced) {
            selectedCard = null;
            return;
        }

        EventDistributor.send(Endpoint.GameScreen, Endpoint.GameScreen,
                new ReqCardSelectedEvent(position));
    }

    public void render(GameState gameState, Player player) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        updateInput();

        renderMiddleCards();
        renderCornerCards();

        renderPlayers(gameState.getPlayers().values(), player);
        renderProperty(gameState.getProperty(), player);
        updateSelection();

        renderMiddleCardsInfo();

        batch.end();
    }

    private void renderMiddleCards() {
        Array<Sprite> sprites = middleCards;
        for (int row = 0; row < 4; row++) {
            int angle = 270 - 90 * row;

            for (int i = 0; i < 9; i++) {
                Vector2 position = getMiddleCardPosition(row, i);
                Sprite sprite = sprites.get(row * 9 + i);

                sprite.setPosition(position.x, position.y);

                sprite.setOrigin(0, sprite.getHeight());
                sprite.setRotation(angle);
                sprite.setOrigin(0, 0);

                sprite.setScale(scalingRatio);

                sprite.draw(batch);
            }
        }
    }

    private void renderMiddleCardsInfo() {
        Array<Sprite> sprites = middleCards;
        Sprite template = sprites.get(0);
        float templateHeight = template.getHeight() * scalingRatio;

        Matrix4 oldTransformMatrix = batch.getTransformMatrix().cpy();

        for (int row = 0; row < 4; row++) {
            for (int i = 0; i < 9; i++) {
                int angle = 270 - 90 * row;

                float offsetX = 0, offsetY = 0;
                switch (angle) {
                    case 0:
                        offsetX = 0;
                        offsetY = templateHeight;
                        break;
                    case 90:
                        offsetX = -templateHeight;
                        offsetY = 0;
                        break;
                    case 180:
                        offsetX = 0;
                        offsetY = -templateHeight;
                        break;
                    case 270:
                        offsetX = templateHeight;
                        offsetY = 0;
                        break;
                }

                Vector2 position = getMiddleCardPosition(row, i);
                position.x += offsetX;
                position.y += offsetY;

                Matrix4 fontTransformMatrix = new Matrix4();
                fontTransformMatrix.rotate(new Vector3(0, 0, 1), angle);
                fontTransformMatrix.trn(position.x, position.y, 0);
                batch.setTransformMatrix(fontTransformMatrix);

                font.draw(batch, row + ":" + i, 0, 0);
            }
        }

        batch.setTransformMatrix(oldTransformMatrix);
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

            float x = constraints.getX(), y = constraints.getY();
            switch (i) {
                case 0:
                    x += 0;
                    y += 0;
                    break;
                case 1:
                    x += 0;
                    y += templateHeight + middleTemplateWidth * 9;
                    break;
                case 2:
                    x += templateWidth + middleTemplateWidth * 9;
                    y += templateHeight + middleTemplateWidth * 9;
                    break;
                case 3:
                    x += templateWidth + middleTemplateWidth * 9;
                    y += 0;
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

    private void renderPlayers(Collection<Player> players, Player player) {
        int i = 0;
        for (Player p : players) {
            int position = p.getPosition();
            Sprite card = allCards.get(position);

            Rectangle bounds = card.getBoundingRectangle();

            float centerX = bounds.x + bounds.width / 2;
            float centerY = bounds.y + bounds.height / 2;

            Texture texture = null;
            if (player.getId().equals(p.getId())) {
                texture = thisPlayerIconTexture;
            } else {
                texture = playerIconTextures.get(i);
            }

            float shiftVal = texture.getWidth() / 2;
            float shiftDeg = i * (float) (Math.PI / players.size());
            float shiftX = shiftVal * (float) Math.cos(shiftDeg);
            float shiftY = shiftVal * (float) Math.sin(shiftDeg);

            batch.draw(texture,
                    centerX - texture.getWidth() / 2 + shiftX,
                    centerY - texture.getHeight() / 2 + shiftY);

            i++;
        }
    }

    private void renderProperty(HashMap<Integer, Property> property, Player player) {
        final float TINT_INTENSITY = 0.75f;

        for (Entry<Integer, Property> e : property.entrySet()) {
            int position = e.getKey();
            Property p = e.getValue();

            Sprite card = allCards.get(position);
            String owner = p.getOwner();

            if (owner == null) {
                card.setColor(new Color(1, 1, 1, 1));
            }

            else if (owner.equals(player.getId())) {
                if (!p.isPledged()) {
                    card.setColor(new Color(TINT_INTENSITY, 1, TINT_INTENSITY, 1));
                } else {
                    card.setColor(new Color(TINT_INTENSITY, TINT_INTENSITY, TINT_INTENSITY, 1));
                }
            }

            else {
                card.setColor(new Color(1, TINT_INTENSITY, TINT_INTENSITY, 1));
            }
        }
    }

    private Sprite getHoverCard() {
        float x = Gdx.input.getX();
        float y = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (!(constraints.getX() <= x && x <= constraints.getX() + constraints.getWidth()) ||
                !(constraints.getY() <= y && y <= constraints.getY() + constraints.getHeight())) {
            return null;
        }

        double minDistance = Float.MAX_VALUE;
        Sprite closestCard = null;
        for (Sprite card : allCards) {
            Rectangle bounds = card.getBoundingRectangle();

            float centerX = bounds.x + bounds.width / 2;
            float centerY = bounds.y + bounds.height / 2;

            double distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
            if (minDistance > distance) {
                minDistance = distance;
                closestCard = card;
            }
        }

        if (minDistance > 100) {
            return null;
        }

        return closestCard;
    }

    private void updateInput() {
        Sprite hoverCard = getHoverCard();

        if (hoverCard != null && Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            selectedCard = hoverCard;

            Integer position = null;
            for (int i = 0; i < allCards.size; i++) {
                if (allCards.get(i) == selectedCard) {
                    position = i;
                    break;
                }
            }

            forceUpdateSelection(position);
        }
    }

    private void updateSelection() {
        for (Sprite card : allCards) {
            card.setAlpha(1.0f);
        }

        if (selectedCard != null) {
            selectedCard.setAlpha(0.75f);
        }
    }

    private Vector2 getMiddleCardPosition(int side, int pos) {
        Array<Sprite> sprites = middleCards;

        Sprite template = sprites.get(0);
        float templateWidth = template.getWidth() * scalingRatio;
        float templateHeight = template.getHeight() * scalingRatio;

        float x = constraints.getX(), y = constraints.getY();
        switch (side) {
            case 0:
                x += 0;
                y += templateHeight + (templateWidth + pos * templateWidth);
                break;
            case 1:
                x += templateHeight + (templateWidth + pos * templateWidth);
                y += templateHeight + (templateHeight + 9 * templateWidth);
                break;
            case 2:
                x += templateHeight + (templateHeight + 9 * templateWidth);
                y += templateHeight + (9 * templateWidth - templateWidth - pos * templateWidth);
                break;
            case 3:
                x += templateHeight + (9 * templateWidth - templateWidth - pos * templateWidth);
                y += 0;
                break;
        }

        return new Vector2(x, y);
    }

}
