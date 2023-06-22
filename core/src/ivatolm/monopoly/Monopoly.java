package ivatolm.monopoly;

import com.badlogic.gdx.Game;
import ivatolm.monopoly.screen.GameScreen;

public class Monopoly extends Game {

	GameScreen gameScreen;

	@Override
	public void create() {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}

}
