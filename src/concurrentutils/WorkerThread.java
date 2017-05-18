package concurrentutils;

/**
 * Created by лёня on 03.04.2017.
 */
class WorkerThread implements Runnable {
    private final ThreadPool threadPool;
    private Runnable currentTask = null;
    private final Object lock = new Object();
    private boolean isAlive;
    private Thread thread;
    WorkerThread (ThreadPool threadPool) {
        this.threadPool = threadPool;
        thread = new Thread(this);
        thread.start();
    }
    public void run() {
        synchronized (lock) {
            while (true) {
                try {
                    while (currentTask == null)
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace ();
                        }
                    currentTask.run();
                    currentTask = null;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    threadPool.onTaskCompleted(this);
                }
            }
        }
    }
    void execute (Runnable task) {
        synchronized (lock) {
            currentTask = task;
            lock.notifyAll();
        }
    }

    void stop () {
        if (isAlive) {
            isAlive = false;
            while (null != currentTask);
            thread.interrupt();
        }
    }
}
