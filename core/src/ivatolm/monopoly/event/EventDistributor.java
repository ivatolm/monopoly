package ivatolm.monopoly.event;

import java.util.HashMap;

public class EventDistributor {

    private static HashMap<EventReceiver.Type, EventReceiver> handlers = new HashMap<>();

    public static void register(EventReceiver.Type type, EventReceiver handler) {
        handlers.put(type, handler);
    }

    public static synchronized void send(
            EventReceiver.Type sender,
            EventReceiver.Type destination,
            MonopolyEvent event) {

        event.setSender(sender);

        EventReceiver receiver = handlers.get(destination);
        receiver.receive(event);
    }

}
