package app;

import netutils.MessageHandler;

/**
 * Created by лёня on 07.04.2017.
 */

public class PrintMessageHandler implements MessageHandler {
    @Override
    public void handle (String message) {
        System.out.println (message);
    }
}
