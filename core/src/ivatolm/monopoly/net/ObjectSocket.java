package ivatolm.monopoly.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.net.Socket;

import ivatolm.monopoly.event.MonopolyEvent;

public class ObjectSocket {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ObjectSocket(Socket socket) {
        this.socket = socket;

        try {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {

            // Should be impossible
            e.printStackTrace();
        }
    }

    public MonopolyEvent receive() throws IOException {
        try {
            return (MonopolyEvent) input.readObject();
        } catch (ClassNotFoundException e) {
            // Should be impossible
            e.printStackTrace();
            return null;
        }
    }

    public void send(MonopolyEvent event) throws IOException {
        output.writeObject(event);
    }

    public void dispose() {
        socket.dispose();
    }

}
