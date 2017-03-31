import java.io.*;
import java.net.Socket;

/**
 * Created by лёня on 31.03.2017.
 */
public class Session implements Runnable {
    private Server server;
    private Socket socket;
    private int id;
    public Session(Server server, Socket socket, int id) {
        this.socket = socket;
        this.server = server;
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Client #" + this.id + " has connected");
        try {
            InputStream inputStream = this.socket.getInputStream();
            OutputStream outputStream = this.socket.getOutputStream();

            DataInputStream dataInputStream = new DataInputStream(inputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            String inMsg;

            while (true)
            {
                inMsg = dataInputStream.readUTF();

                if (inMsg.equals("exit") )
                {
                    System.out.println("Client #" + this.id + " has gone.");
                    break;
                }
                System.out.println("Client #" + this.id + ": " + inMsg);

                dataOutputStream.writeUTF("success");
                dataOutputStream.flush();

            }
        } catch (IOException e) {
            System.out.println("Client #" + this.id + " connection aborted.");
        } finally {
            System.out.println("Client #" + this.id + " session finished.");
            server.threadStop();
        }
    }
}
