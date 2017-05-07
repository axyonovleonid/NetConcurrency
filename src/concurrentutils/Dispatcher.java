package concurrentutils;

import netutils.Session;

/**
 * Created by лёня on 02.04.2017.
 */
public class Dispatcher implements Runnable {
    private final Channel<Session> channel;
    private final ThreadPool threadPool;
    private boolean isAlive;

    public Dispatcher(Channel<Session> channel, ThreadPool threadPool) {
        this.channel = channel;
        this.threadPool = threadPool;
    }
    public void run() {
        isAlive = true;
        while (isAlive) {
                threadPool.execute (channel.take ());
        }
    }

    public void stop () {
        isAlive = false;
    }
}