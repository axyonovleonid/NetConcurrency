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
//            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream (socket.getInputStream ());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            String outMsg = "";
            String inMsg;

            List<Teacher> teachers = new ArrayList<> ();
            try{
                while (!outMsg.equals ("exit")) {
                    outMsg = bufferedReader.readLine();
                    if(!outMsg.isEmpty()) {
                        dataOutputStream.writeUTF (outMsg);
                        System.out.println ("Send!");



                        int size = (int) objectInputStream.readObject ();
                        if(size == -222) {
                            System.out.println((String) objectInputStream.readObject());
                        }
                        else if(size == -404){
                            System.out.println("Error");
                        }
                        while(size > 0) {
                            try {
                                  Teacher tc = (Teacher) objectInputStream.readObject ();
                                  System.out.println(tc);
                                  size--;
                            } catch (ClassNotFoundException | IOException e) {
                                e.printStackTrace ();
                                System.out.println("end of stream");
                                break;
                            }
                        }
                    }
                }
                System.out.println("List of teacher's:");
                System.out.println (teachers.toString ());
            }
            catch (SocketException e){
                System.out.println ("Server's disconnected");
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }
        }  catch (IllegalArgumentException str){
            System.err.print("Illegal port");
        }
    }
}