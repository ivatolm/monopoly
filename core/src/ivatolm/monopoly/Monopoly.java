package ivatolm.monopoly;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;

import ivatolm.monopoly.event.EventReceiver;
import ivatolm.monopoly.event.MonopolyEvent;
import ivatolm.monopoly.net.MonopolyClient;
import ivatolm.monopoly.net.MonopolyServer;
import ivatolm.monopoly.event.EventDistributor;
import ivatolm.monopoly.screen.BaseScreen;
import ivatolm.monopoly.screen.CreateLobbyScreen;
import ivatolm.monopoly.screen.GameScreen;
import ivatolm.monopoly.screen.JoinLobbyScreen;
import ivatolm.monopoly.screen.LobbyScreen;
import ivatolm.monopoly.screen.MainMenuScreen;

public class Monopoly extends Game implements EventReceiver {

	private LinkedList<MonopolyEvent> events;

	private MainMenuScreen mainMenuScreen;
	private JoinLobbyScreen joinLobbyScreen;
	private CreateLobbyScreen createLobbyScreen;
	private LobbyScreen lobbyScreen;
	private GameScreen gameScreen;
	private MonopolyClient client;
	private MonopolyServer server;

	@Override
	public void create() {
		VisUI.load();

		events = new LinkedList<>();

		EventDistributor.register(Endpoint.Game, this);

		mainMenuScreen = new MainMenuScreen();
		EventDistributor.register(Endpoint.MainMenuScreen, mainMenuScreen);

		joinLobbyScreen = new JoinLobbyScreen();
		EventDistributor.register(Endpoint.JoinLobbyScreen, joinLobbyScreen);

		createLobbyScreen = new CreateLobbyScreen();
		EventDistributor.register(Endpoint.CreateLobbyScreen, createLobbyScreen);

		lobbyScreen = new LobbyScreen();
		EventDistributor.register(Endpoint.LobbyScreen, lobbyScreen);

		gameScreen = new GameScreen();
		EventDistributor.register(Endpoint.GameScreen, gameScreen);

		client = new MonopolyClient();
		EventDistributor.register(Endpoint.Client, client);

		server = new MonopolyServer();
		EventDistributor.register(Endpoint.Server, server);

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
			case GoMainMenuScreenEvent:
				setScreen(mainMenuScreen);
				break;
			case GoJoinLobbyScreenEvent:
				setScreen(joinLobbyScreen);
				break;
			case GoCreateLobbyScreenEvent:
				setScreen(createLobbyScreen);
				break;
			case GoLobbyScreenEvent:
				setScreen(lobbyScreen);
				break;
			case GoGameScreenEvent:
				setScreen(gameScreen);
				break;
			case ReqEndGame:
				reset();
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
	}

	@Override
	public void dispose() {
		mainMenuScreen.dispose();
		joinLobbyScreen.dispose();
		createLobbyScreen.dispose();
		lobbyScreen.dispose();
		gameScreen.dispose();

		client.dispose();
		server.dispose();

		VisUI.dispose();

		super.dispose();
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);

		((BaseScreen) screen).setAsInputHandler();
	}

	private void reset() {
		setScreen(mainMenuScreen);

		client.dispose();
		server.dispose();

		client = new MonopolyClient();
		EventDistributor.register(Endpoint.Client, client);

		server = new MonopolyServer();
		EventDistributor.register(Endpoint.Server, server);
	}

}
