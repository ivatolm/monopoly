package ivatolm.monopoly;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.net.Client;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.resource.ResourceManager;
import ivatolm.monopoly.screen.BaseScreen;
import ivatolm.monopoly.screen.JoinLobbyScreen;
import ivatolm.monopoly.screen.LobbyScreen;
import ivatolm.monopoly.screen.MainMenuScreen;

public class Monopoly extends Game implements EventReceiver {

	private LinkedList<MonopolyEvent> events;

	private MainMenuScreen mainMenuScreen;
	private JoinLobbyScreen joinLobbyScreen;
	private LobbyScreen lobbyScreen;
	private Client client;

	@Override
	public void create() {
		events = new LinkedList<>();

		EventDistributor.register(Type.Game, this);

		mainMenuScreen = new MainMenuScreen();
		EventDistributor.register(Type.MainMenuScreen, mainMenuScreen);

		joinLobbyScreen = new JoinLobbyScreen();
		EventDistributor.register(Type.JoinLobbyScreen, joinLobbyScreen);

		lobbyScreen = new LobbyScreen();
		EventDistributor.register(Type.LobbyScreen, lobbyScreen);

		client = new Client();
		EventDistributor.register(Type.Client, client);

		setScreen(mainMenuScreen);
	}

	@Override
	public void receive(MonopolyEvent event) {
		events.add(event);
	}

	@Override
	public void handleEvents() {
		MonopolyEvent event = events.pop();
		switch (event.getType()) {
			case JoinLobby:
				setScreen(joinLobbyScreen);
				break;
			case JoinedLobby:
				setScreen(lobbyScreen);
				break;
			default:
				break;
		}
	}

	@Override
	public void render() {
		super.render();

		if (events.size() > 0) {
			handleEvents();
		}

		client.handleEvents();
	}

	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		joinLobbyScreen.dispose();
		lobbyScreen.dispose();
		client.dispose();
		ResourceManager.dispose();
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);

		((BaseScreen) screen).setAsInputHandler();
	}

}
