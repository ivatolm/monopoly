package ivatolm.monopoly.event;

public interface EventReceiver {

    public static enum Type {
        Game,
        MainMenuScreen,
        JoinLobbyScreen
    }

    public void receive(MonopolyEvent event);

    public void handleEvents();

}
