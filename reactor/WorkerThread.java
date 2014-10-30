package reactor;

import reactorapi.*;

public class WorkerThread<T> extends Thread {
	private final EventHandler<T> handler;
	private final BlockingEventQueue<Object> queue;

	// Additional fields are allowed.

	public WorkerThread(EventHandler<T> eh, BlockingEventQueue<Object> blockingEventQueue) {
		handler = eh;
		queue = blockingEventQueue;
	}

	public void run() {
	  T data = null;
	  Event <T> ev = null;
	  Handle <T> handle = this.handler.getHandle ();
	  
	  while (true)
	  {
	    data = handle.read ();
	    ev = new Event <T> (data, this.handler);
	    try
      {
        this.queue.put (ev);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
	  }
		// TODO: Implement WorkerThread.run().
	}

	public void cancelThread() {
		// TODO: Implement WorkerThread.cancelThread().
	}
}