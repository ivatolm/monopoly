package ivatolm.monopoly.event;

public interface EventReceiver {

    public static enum Endpoint {
        Game,
        MainMenuScreen,
        JoinLobbyScreen,
        LobbyScreen,
        Client,
        Server, CreateLobbyScreen
    }

    public void receive(MonopolyEvent event);

    public void handleEvents();

}
