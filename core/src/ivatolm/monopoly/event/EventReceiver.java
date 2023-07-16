package ivatolm.monopoly.event;

public interface EventReceiver {

    public static enum Endpoint {
        Game,
        MainMenuScreen,
        JoinLobbyScreen,
        CreateLobbyScreen,
        LobbyScreen,
        GameScreen,
        Client,
        Server,
    }

    public void receive(MonopolyEvent event);

    public void handleEvents();

}
