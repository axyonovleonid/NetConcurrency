package app;
/**
 * Created by лёня on 28.03.2017.
 */

import concurrentutils.Channel;
import concurrentutils.Dispatcher;
import concurrentutils.ThreadPool;
import netutils.Host;
import netutils.MessageHandlerFactory;
import netutils.Session;

import java.io.Serializable;

public class Server implements Runnable, Serializable {
    private static final int DEFAULT_CHANNEL_SIZE = 16;

    private static final Object lock = new Object ();
    public static final String SERVER_BUSY_MESSAGE = "Server is busy";
    public static final String SERVER_SUCCESS_MESSAGE = "Success";
    private static int maxThreadCount;
    private final int port;
    private Channel<Session> channel;
    private ThreadPool threadPool;
    private Dispatcher dispatcher;
    private Host host;
    private MessageHandlerFactory classMHF;
    public Server (int port, int maxThreadCount, Class classMHF) {
        this.port = port;
        this.maxThreadCount = maxThreadCount;
        try {
            this.classMHF = (MessageHandlerFactory) classMHF.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace ();
        }
    }

    public void stop(){
        System.out.println("Server's stopped");
        host.stop();
        dispatcher.stop();
        threadPool.stop();
    }

    public void run (){
        System.out.println("Server has started. Sessions limit: " + maxThreadCount);
        channel = new Channel<> (DEFAULT_CHANNEL_SIZE);
        threadPool = new ThreadPool (maxThreadCount);
        dispatcher = new Dispatcher (channel, threadPool);
        new Thread(dispatcher).start ();
        host = new Host (port, channel, threadPool, classMHF);
        host.run();
 //       ChatHistoryMessage.WriteObj ();
 //       ReadObj ();
    }
}