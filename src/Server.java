/**
 * Created by лёня on 28.03.2017.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private static final Object lock = new Object ();
    private static int threadCount = 0;
    private static int maxThreadCount;
    private static int port;

    public Server (int port, int maxThreadCount) {
        Server.port = port;
        Server.maxThreadCount = maxThreadCount;
    }

    public static void increaseThreadCount () {
        synchronized (lock) {
            ++threadCount;
        }
    }

    public static void decreaseThreadCount () {
        synchronized (lock) {
            --threadCount;
        }
    }

    public static int getThreadCount () {
        synchronized (lock) {
            return threadCount;
        }
    }

    public void run () {
        try {
            ServerSocket serverSocket = new ServerSocket (port);
            int sessionId = -1;
            while (true) if (getThreadCount () <= maxThreadCount) {
                Socket socket = serverSocket.accept ();
                increaseThreadCount ();
                Thread thread = new Thread (new Session (this, socket, ++sessionId));
                thread.start ();
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void threadStop () {
        decreaseThreadCount ();
    }


}