package app;

import database.DataBaseCommandExecutor;
import netutils.MessageHandler;

/**
 * NetConcurrency created by лёня on 18.05.2017.
 */
public class CommandMessageHandler implements MessageHandler {
    private DataBaseCommandExecutor dbce;

    CommandMessageHandler(){
        dbce = new DataBaseCommandExecutor ("localhost", 3306, "root",
                "root", "teachers");
    }

    @Override
    public void handle (String message) {
        System.out.println (message);
    }

    @Override
    public Object handleCommand (String message){
        if(message.startsWith ("search")){
            message = message.replace ("search ", "");
            return dbce.searchTeacher (message);
        }
        if(message.startsWith ("call")){
            message = message.replace ("call ", "");
            return dbce.checkCoord (message);
        }
        return 0;
    }
}