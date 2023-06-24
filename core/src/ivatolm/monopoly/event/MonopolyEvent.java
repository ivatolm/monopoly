package ivatolm.monopoly.event;

public abstract class MonopolyEvent {

    public enum Type {
        JoinLobby,
        ConnectLobby
    }

    private Type type;

    public MonopolyEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

}
