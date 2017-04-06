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

public class Server implements Runnable {
    private static final int DEFAULT_CHANNEL_SIZE = 16;

    private static final Object lock = new Object ();
    public static final String SERVER_BUSY_MESSAGE = "Server is busy";
    public static final String SERVER_SUCCESS_MESSAGE = "Success";
    private static int maxThreadCount;
    private final int port;
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

    public void run (){
        System.out.println("Server has started. Sessions limit: " + maxThreadCount);
        Channel<Session> channel = new Channel<> (DEFAULT_CHANNEL_SIZE);
        ThreadPool threadPool = new ThreadPool (maxThreadCount);
        Dispatcher dispatcher = new Dispatcher (channel, threadPool);
        new Thread(dispatcher).start ();
        Host host = new Host (port, channel, threadPool, classMHF);
        host.run();
    }
}