package ivatolm.monopoly.event;

public abstract class MonopolyEvent {

    public enum Type {
        // navigation
        GoJoinLobbyScreenEvent,
        GoLobbyScreenEvent,
        GoCreateLobbyScreenEvent,

        // request
        ConnectLobby,
        CreateLobby,

        // response
        JoinedLobby,
        CreatedLobby,
        ClientConnected,
        ServerAccepted,
    }

    private Type type;
    private EventReceiver.Type sender;
    private boolean result;
    private String errorMsg;

    public MonopolyEvent(Type type) {
        this.type = type;
    }

    public void setSender(EventReceiver.Type sender) {
        this.sender = sender;
    }

    public Type getType() {
        return type;
    }

    public EventReceiver.Type getSender() {
        return sender;
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

    @Override
    public String toString() {
        return "ME { type:" + type + ", sender:" + this.sender + ", result:" + result + " }";
    }

}
