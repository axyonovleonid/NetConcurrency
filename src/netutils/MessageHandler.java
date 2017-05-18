package netutils;

/**
 * Created by лёня on 07.04.2017.
 */
public interface MessageHandler {
    void handle(String message);
    Object handleCommand(String message);
}
