package ivatolm.monopoly.event;

import java.io.Serializable;

public abstract class MonopolyEvent implements Serializable {

    public enum Type {
        // navigation
        GoJoinLobbyScreenEvent,
        GoLobbyScreenEvent,
        GoCreateLobbyScreenEvent,

        // request
        ReqConnectToLobby,
        ReqCreateLobby,
        ReqUpdateLobbyInfoEvent,
        ReqInitClientEvent,

        // response
        RespJoinedLobby,
        RespLobbyCreated,
        RespClientConnected,
        RespClientDisconnected,
        RespClientAccepted,

        // network
        ReqConnectEvent,
        ReqDisconnectEvent,
    }

    private Type type;
    private transient EventReceiver.Endpoint sender;
    private transient EventReceiver.Endpoint receiver;
    private boolean result;
    private String errorMsg;

    public MonopolyEvent(Type type) {
        this.type = type;
    }

    public void setSender(EventReceiver.Endpoint sender) {
        this.sender = sender;
    }

    public void setReceiver(EventReceiver.Endpoint receiver) {
        this.receiver = receiver;
    }

    public Type getType() {
        return type;
    }

    public EventReceiver.Endpoint getSender() {
        return sender;
    }

    public EventReceiver.Endpoint getReceiver() {
        return receiver;
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
        return "ME { type:" + type + ", sender:" + this.sender + ", receiver:" + this.receiver + ", result:" + result
                + " }";
    }

}
