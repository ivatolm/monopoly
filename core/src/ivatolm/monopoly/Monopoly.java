package ivatolm.monopoly;

import com.badlogic.gdx.Game;

import ivatolm.monopoly.resources.ResourceManager;
import ivatolm.monopoly.screens.MainMenuScreen;

public class Monopoly extends Game {

	MainMenuScreen mainMenuScreen;

	@Override
	public void create() {
		mainMenuScreen = new MainMenuScreen();
		setScreen(mainMenuScreen);
	}

	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		ResourceManager.dispose();
	}

}
