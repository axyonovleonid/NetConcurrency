package app;

import netutils.MessageHandler;
import netutils.MessageHandlerFactory;

/**
 * NetConcurrency created by лёня on 18.05.2017.
 */
public class CommandMessageHandlerFactory implements MessageHandlerFactory {
    @Override
    public MessageHandler create(){
        return new CommandMessageHandler ();
    }
}
