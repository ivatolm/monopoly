package ivatolm.monopoly.event;

public abstract class MonopolyEvent {

    public enum Type {
        JoinLobby,
        ConnectLobby
    }

    private Type type;
    private String errorMsg;

    public MonopolyEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

}
