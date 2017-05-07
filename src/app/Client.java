package app; /**
 * Created by лёня on 28.03.2017.
 */

import database.Teacher;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class Client {
    public static void main(String[] args) throws IOException {

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            if(port < 0)
                throw new IllegalArgumentException(args[1]);

            Socket socket = new Socket(host, port);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//            ObjectInputStream objectInputStream = new ObjectInputStream (socket.getInputStream ());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            String outMsg;
            String inMsg;

            List<Teacher> teachers = new ArrayList<> ();
            try{
                while (true) {
                    outMsg = bufferedReader.readLine();


                    dataOutputStream.writeUTF(outMsg);
                    dataOutputStream.flush();

                    inMsg = dataInputStream.readUTF();
                    System.out.println(inMsg);

                    }
            }
            catch (SocketException e){
                e.printStackTrace();
                System.out.print (teachers);
            }
        }  catch (IllegalArgumentException str){
            System.err.print("Illegal port");
        }
    }
}