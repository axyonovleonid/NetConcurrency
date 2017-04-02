/**
 * Created by лёня on 02.04.2017.
 */
public class Dispatcher implements Runnable {
    private final Channel channel;
    private final Server server;
    public Dispatcher(Channel channel, Server server) {
        this.channel = channel;
        this.server = server;
    }
    public void run() {
        while (true) {
            Session session = (Session)channel.take();
            try {
                new Thread(session).start();
            } catch (SecurityException | IllegalThreadStateException e) {
                System.out.println(e.getMessage());
                server.threadFailed(session.getId());
            }
        }
    }
}