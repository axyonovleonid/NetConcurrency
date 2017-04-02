import java.util.LinkedList;

/**
 * Created by лёня on 02.04.2017.
 */
public class Channel {
    private final int maxSize;
    private final LinkedList<Runnable> queue = new LinkedList<>();
    private final Object lock = new Object();
    private Runnable x;

    public Channel(int maxSize) {
        this.maxSize = maxSize;
    }
    void put(Runnable x) throws InterruptedException{
        this.x = x;
        synchronized (lock) {
            while (queue.size() == maxSize)
                try {
                    lock.wait();
                }
                
                catch (InterruptedException e){
                    System.err.println (e);
                }
            queue.addLast(x);
            lock.notifyAll();
        }
    }
    Runnable take() {
        synchronized (lock) {
            while (queue.isEmpty())
                try { lock.wait(); }
                catch (InterruptedException e) {}
            lock.notifyAll();
            return queue.removeFirst();
        }
    }
}
