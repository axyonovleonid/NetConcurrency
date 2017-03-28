/**
 * Created by лёня on 28.03.2017.
 */
import java.io.*;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            if(port < 0) throw new IllegalArgumentException(args[1]);

            Socket socket = new Socket(host, port);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String msg;

            while (true) {
                msg = bufferedReader.readLine();
                dataOutputStream.writeUTF(host + "-" + port + ": " + msg);
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException str){
            System.err.print("Illegal port");
        }
    }
}