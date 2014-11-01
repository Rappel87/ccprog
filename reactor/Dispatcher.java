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
		map   = new HashMap<EventHandler<?>, WorkerThread<?>> ();
	}

	public void handleEvents() throws InterruptedException {
	  Event <?> ev = null;
	  EventHandler <Event> eh = null;
	  
	  ev = select ();
	  eh = (EventHandler<Event>) ev.getHandler ();
	   
	  /* we need to pass the "event"(socket/message), but not possible in the generic way... */
	  eh.handleEvent ((Event) ev.getEvent ());

		// TODO: Implement Dispatcher.handleEvents().
	}
  
	public Event<?> select() throws InterruptedException {
		return queue.get ();
	}

	public <T> void addHandler(EventHandler<?> h) {
	  WorkerThread <T> thread = new WorkerThread<T> ((EventHandler<T>) h, queue);
	  map.put (h, thread);
	  
	  thread.start ();
	}

	public void removeHandler(EventHandler<?> h) {
		WorkerThread <?> thread = null;
		
		thread = map.get (h);
		thread.cancelThread ();
		
		map.remove (h);
	}
	
	public void addHandlerThreadMapping (EventHandler<?> eh, WorkerThread <?> thread)
	{
	  map.put (eh, thread);
	}

	public BlockingEventQueue<Object> getQueue ()
	{
	  return this.queue;
	}

	public HashMap<EventHandler <?>, WorkerThread <?>> getMap ()
	{
	  return map;
	}
	// Add methods and fields as needed.
}
