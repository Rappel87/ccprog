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
    EventHandler<?> eh = null;

    while (!map.isEmpty ())
    {
      ev = select ();
      eh = ev.getHandler ();
      
      /* remove the handler, if event is null */
      if (ev.getEvent () == null)
      {
        removeHandler (eh);
        /* just get the next event */
        continue;
      }
      /* we need to pass the "event"(socket/message), but not possible in the generic way... */
      //eh.handleEvent ((Event) ev.getEvent ());
      /* Only dispatch if the handler is still registered */
      if ((eh != null) && (map.containsKey (eh)))
      {
        ev.handle ();
      }
    }
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
    if (thread != null)
    {
      thread.cancelThread ();
    }
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
