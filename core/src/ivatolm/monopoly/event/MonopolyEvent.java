package ivatolm.monopoly.event;

public abstract class MonopolyEvent {

    public enum Type {
        // navigation
        GoJoinLobbyScreenEvent,
        GoLobbyScreenEvent,
        GoCreateLobbyScreenEvent,

        // request
        ReqConnectToLobby,
        ReqCreateLobby,

        // response
        RespJoinedLobby,
        RespLobbyCreated,
        RespClientConnected,
        RespClientAccepted,
    }

    private Type type;
    private EventReceiver.Endpoint sender;
    private boolean result;
    private String errorMsg;

    public MonopolyEvent(Type type) {
        this.type = type;
    }

    public void setSender(EventReceiver.Endpoint sender) {
        this.sender = sender;
    }

    public Type getType() {
        return type;
    }

    public EventReceiver.Endpoint getSender() {
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
