package ivatolm.monopoly;

import java.util.LinkedList;

import com.badlogic.gdx.Game;

import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.resource.ResourceManager;
import ivatolm.monopoly.screen.MainMenuScreen;

public class Monopoly extends Game implements EventReceiver {

	private LinkedList<MonopolyEvent> events;
	private MainMenuScreen mainMenuScreen;

	@Override
	public void create() {
		events = new LinkedList<>();

		EventDistributor.register(Type.Game, this);

		mainMenuScreen = new MainMenuScreen();
		EventDistributor.register(Type.MainMenuScreen, mainMenuScreen);

		setScreen(mainMenuScreen);
	}

	@Override
	public void receive(MonopolyEvent event) {
		events.add(event);
	}

	@Override
	public void handleEvents() {
		if (events.isEmpty()) {
			return;
		}

		MonopolyEvent event = events.pop();
		switch (event) {
			case JoinLobby:
				
				break;
		}
	}

	@Override
	public void render() {
		super.render();

		handleEvents();
	}

	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		ResourceManager.dispose();
	}

}
