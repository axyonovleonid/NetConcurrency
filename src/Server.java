/**
 * Created by лёня on 28.03.2017.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private static final Object lock = new Object ();
    public static final String SERVER_BUSY_MESSAGE = "Server is busy";
    public static final String SERVER_SUCCES_MESSAGE = "Succes";
    private static final int DEFAULT_CHANNEL_SIZE = 16;

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

    public void run (){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Channel channel = new Channel(DEFAULT_CHANNEL_SIZE);
            Dispatcher dispatcher = new Dispatcher (channel, this);
            new Thread(dispatcher).start ();
            System.out.println("Server has started. Sessions limit: " + maxThreadCount);
            int sessionID = -1;
            while (true) {
                Socket socket = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                synchronized (lock) {
                    if (getThreadCount () == maxThreadCount) {
                        dataOutputStream.writeUTF(SERVER_BUSY_MESSAGE);
                        do {
                            try {
                                lock.wait();
                            }
                            catch (InterruptedException e) {
                                System.out.println(e.getMessage());
                            }
                        } while (getThreadCount () == maxThreadCount);
                    }
                    increaseThreadCount ();
                    Session session = new Session(this, socket, ++sessionID);
                    channel.put(session);
                    dataOutputStream.writeUTF(SERVER_SUCCES_MESSAGE);
                    System.out.println("Client #" + sessionID + " connected. Sessions count: " + getThreadCount ());
                }
            }

        } catch (IOException e) {
            e.printStackTrace ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public void threadStop (int sessionID) {
        if (threadCount == maxThreadCount)
            synchronized (lock) {
                lock.notifyAll();
            }
        decreaseThreadCount ();
        System.out.println("Client #" + sessionID + " has disconected." +
                "Count of running sessions: " + getThreadCount ());
    }

    public void threadFailed (int sessionID) {
        System.err.println("Session #" + sessionID + " failed");
        threadStop(sessionID);
    }
}