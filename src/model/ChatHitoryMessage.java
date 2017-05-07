package model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by лёня on 21.04.2017.
 */
public class ChatHitoryMessage implements Serializable{
    private LocalDateTime time;
    private String userName;
    private String message;
    public ChatHitoryMessage (LocalDateTime time, String userName, String message){
        this.time = time;
        this.userName = userName;
        this.message = message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
    public void setUserName (String userName) {
        this.userName = userName;
    }

    public void setTime (LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime () {
        return time;
    }
    public String getMessage () {
        return message;
    }
    public String getUserName(){
        return userName;
    }
    public void WriteObj(){
        try {
            FileOutputStream out = new FileOutputStream ("output.out");
            ObjectOutputStream oos = new ObjectOutputStream (out);
            ChatHitoryMessage message = new ChatHitoryMessage(LocalDateTime.now(), "name", "hello");
            oos.writeObject (message);
            out.flush ();
            out.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    public void ReadObj(){
        try{
            FileInputStream in = new FileInputStream ("output.out");
            ObjectInputStream ois = new ObjectInputStream (in);
            ChatHitoryMessage message = (ChatHitoryMessage) ois.readObject ();
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace ();
        }
    }
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ChatHitoryMessage)) return false;
        if(this == obj) return true;
//        ChatHistoryMessage msg = (ChatHitoryMessage) obj;
        ChatHitoryMessage q =  ((ChatHitoryMessage) obj);
        return Objects.equals (message, q.getMessage ()) && userName.equals (q.getUserName ()) && time.equals (q.getTime ());
    }

}
