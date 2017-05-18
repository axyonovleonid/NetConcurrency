package netutils;

import database.Teacher;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
  //          OutputStream outputStream = this.socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream (socket.getOutputStream ());
            DataInputStream dataInputStream = new DataInputStream(inputStream);
 //           DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            String inMsg;
            while (true)
            {
                inMsg = dataInputStream.readUTF();
                if (inMsg.equals("exit") ){
                    classMH.handle (inMsg);
                    break;
                }

                if (!inMsg.isEmpty ()) {
                    classMH.handle ("Client #" + this.id + ": " + inMsg);
                    Object obj = classMH.handleCommand (inMsg);

                    if(obj.getClass ().toString ().equals ("class java.util.ArrayList")) {
                        List<Teacher> teacherList = (ArrayList<Teacher>) obj;
                        objectOutputStream.writeObject (teacherList.size ());
                        if(teacherList.size () != 0){
                            for(Teacher tc:teacherList){
                                classMH.handle (tc.toString ());
                                objectOutputStream.writeObject (tc);
                                objectOutputStream.flush();
                            }
                        }
                    }
                    else if(obj.getClass ().toString ().equals ("class java.lang.String")){
                        String res = (String) obj;
                        objectOutputStream.writeObject(-222);
                        objectOutputStream.writeObject (res);
                    }
                    else {
                        objectOutputStream.writeObject(-404);
                        classMH.handle ("Client #" + this.id + ": Error");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client #" + this.id + " connection aborted.");
        } finally {
            System.out.println("Client #" + this.id + " session finished.");

        }
    }


}
