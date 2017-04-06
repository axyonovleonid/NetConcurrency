package app;

import netutils.MessageHandler;
import netutils.MessageHandlerFactory;

/**
 * Created by лёня on 07.04.2017.
 */

public class PrintMessageHandlerFactory implements MessageHandlerFactory {
    @Override
    public MessageHandler create(){
        return new PrintMessageHandler ();
    }
}
