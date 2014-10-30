package reactor;

import java.util.HashMap;

import reactorapi.*;

public class Dispatcher {
  private BlockingEventQueue<Object> queue;
  private HashMap<EventHandler <?>, WorkerThread <?>> map;
  
  public Dispatcher() {
		this(10);
	}

	public Dispatcher(int capacity) {
		queue = new BlockingEventQueue<Object> (capacity);
	}

	public void handleEvents() throws InterruptedException {
	  Event <?> ev = null;
	  EventHandler <Event> eh = null;
	  
	  ev = select ();
	  eh = (EventHandler<Event>) ev.getHandler ();
	  
	  eh.handleEvent ((Event) ev.getEvent ());

		// TODO: Implement Dispatcher.handleEvents().
	}

	public Event<?> select() throws InterruptedException {
		return queue.get ();
	}

	public void addHandler(EventHandler<?> h) {
		// TODO: Implement Dispatcher.addHandler(EventHandler).
	}

	public void removeHandler(EventHandler<?> h) {
		// TODO: Implement Dispatcher.removeHandler(EventHandler).
	}
	
	public void addHandlerThreadMapping (EventHandler<?> eh, WorkerThread <?> thread)
	{
	  map.put (eh, thread);
	}

	public BlockingEventQueue<Object> getQueue ()
	{
	  return this.queue;
	}
	// Add methods and fields as needed.
}
