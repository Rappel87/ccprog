package reactor;

import reactorapi.BlockingQueue;

import java.util.LinkedList;
import java.util.List;

import hangman.Semaphore;

public class BlockingEventQueue<T> implements BlockingQueue<Event<? extends T>> {

  private static final boolean DEBUG = true;
  private Semaphore lockP;
  private Semaphore lockG;
  private Semaphore full;
  private Semaphore empty;
  private LinkedList <Event<? extends T>> queue;
  private int capacity;

  public BlockingEventQueue(int capacity) {
    /* 
     * First thread should be possible to aquire lock 
     * Keep track that only 1 thread accesses queue
     */
    lockP         = new Semaphore (1);  
    lockG         = new Semaphore (1);
    full          = new Semaphore (capacity);
    empty         = new Semaphore (0);
    queue         = new LinkedList<Event<? extends T>> ();
    this.capacity = capacity;
  }

  public int getSize() {
    int tmp;

    /* Check that no thread is putting or getting an element */
    try
    {
      lockP.acquire ();
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try
    {
      lockG.acquire ();
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    tmp = queue.size ();

    lockP.release ();
    lockG.release ();

    return tmp;
  }

  public int getCapacity() {
    return capacity;
  }

  public Event<? extends T> get() throws InterruptedException {
    Event<? extends T> tmp;

    /* If queue is empty it should block here */
    if (DEBUG)
    {
      System.out.println ("Check if queue is empty");
    }

    if (DEBUG)
    {
      System.out.println ("not empty");
    }

    empty.acquire ();
    /* Start of CS */
    lockG.acquire ();
    tmp = (Event<? extends T>) queue.removeFirst ();
    lockG.release ();
    /* End of CS */

    full.release ();

    if (DEBUG)
    {
      System.out.println ("remove:" + tmp.getEvent ());
    }   
    return (Event<? extends T>) tmp;
  }

  public synchronized List<Event<? extends T>> getAll() {
    throw new UnsupportedOperationException(); // Replace this.
    // TODO: Implement BlockingEventQueue.getAll().
  }

  public void put(Event<? extends T> event) throws InterruptedException {

    full.acquire ();

    /* Start CS */
    lockP.acquire ();
    queue.addLast ((Event<? extends T>) event);
    lockP.release ();

    /* One element was added, increase the count of empty */
    empty.release ();

    /* End CS */
    if (DEBUG)
    {
      System.out.println ("put:" + event.getEvent ());
    }
  }


  // Add other methods and variables here as needed.
}