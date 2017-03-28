/**
 * Created by лёня on 28.03.2017.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
//    private int port;
//
//    Server(){
//        this.port = 2222;
//    }
//    Server(int port){
//        this.port = port;
//    }
    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);
        try {
            if(port < 0) throw new IllegalArgumentException(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String message;

            while (true) {
                message = dataInputStream.readUTF();

//                if (message == "exit") socket.close(); TODO fix it
                System.out.println(message);

                dataOutputStream.writeUTF("succes");
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException str){
            System.err.print("Illegal port");
        }
    }


}