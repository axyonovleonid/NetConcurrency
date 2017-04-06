package netutils;

import app.Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by лёня on 31.03.2017.
 */
public class Session implements Runnable {
    private final Socket socket;
    private final int id;
    private MessageHandler classMH;
    public Session (Socket socket, int id, MessageHandler classMH) {
        this.socket = socket;
        this.id = id;
        this.classMH = classMH;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = this.socket.getInputStream();
            OutputStream outputStream = this.socket.getOutputStream();

            DataInputStream dataInputStream = new DataInputStream(inputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            String inMsg;

            while (true)
            {
                inMsg = dataInputStream.readUTF();

                if (inMsg.equals("exit") ) break;

                classMH.handle ("Client #" + this.id + ": " + inMsg);

                dataOutputStream.writeUTF(Server.SERVER_SUCCESS_MESSAGE);
                dataOutputStream.flush();

            }
        } catch (IOException e) {
            System.out.println("Client #" + this.id + " connection aborted.");
        } finally {
            System.out.println("Client #" + this.id + " session finished.");

        }
    }


}
