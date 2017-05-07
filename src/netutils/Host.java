package netutils; /**
 * Created by лёня on 06.04.2017.
 */
import concurrentutils.Channel;
import concurrentutils.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {
    private int port;
    private Channel<Session> channel;
    private static int threadCount;
    private static final Object lock = new Object ();
    private ThreadPool threadPool;
    MessageHandler classMH;
    private boolean isAlive;

    public Host (int port, Channel<Session> channel, ThreadPool threadPool, MessageHandlerFactory classMHF){
        this.port = port;
        this.channel = channel;
        threadCount = 0;
        this.threadPool = threadPool;
        classMH = classMHF.create();
    }

    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket (port);
            isAlive = true;
            int sessionID = -1;

            while (isAlive) try {
                Socket socket = serverSocket.accept ();

                synchronized (lock) {
                    Session session = new Session (socket, ++sessionID, classMH);
                    threadPool.execute (session);
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    public void sessionStarted(int sessionID) {
        synchronized (lock) {
            ++threadCount;
            System.out.println("Client #" + sessionID + " has connected." +
                    "Count of running sessions: " + threadCount);
        }
    }
    public void sessionMessage(int sessionID, String message) {
        System.out.println("Client #" + sessionID + ": " + message);
    }
    public void sessionFinished(int sessionID) {
        synchronized (lock) {
            --threadCount;
            System.out.println("Client #" + sessionID + " has disconnected." +
                    "Count of running sessions: " + threadCount);
        }
    }

    public void stop () {
        isAlive = false;
    }
}
