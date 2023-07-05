package ivatolm.monopoly.event;

import java.util.HashMap;

public class EventDistributor {

    private static HashMap<EventReceiver.Endpoint, EventReceiver> handlers = new HashMap<>();

    public static void register(EventReceiver.Endpoint type, EventReceiver handler) {
        handlers.put(type, handler);
    }

    public static synchronized void send(
            EventReceiver.Endpoint sender,
            EventReceiver.Endpoint destination,
            MonopolyEvent event) {

        event.setSender(sender);
        event.setReceiver(destination);

        EventReceiver receiver = handlers.get(destination);
        receiver.receive(event);
    }

}
