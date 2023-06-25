package ivatolm.monopoly.event;

public abstract class MonopolyEvent {

    public enum Type {
        JoinLobby,
        ConnectLobby,
        JoinedLobby,
        StartLobby,
        ClientConnected,
        ServerAccepted
    }

    private Type type;
    private boolean result;
    private String errorMsg;

    public MonopolyEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
