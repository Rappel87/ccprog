/**
 * Semaphore is a straightforward implementation of the well-known
 * synchronization primitive. Its counter can be initialized to any
 * nonnegative value -- by default, it is zero.
 */
package hangman;

public class Semaphore {
  private int counter;
  public Semaphore() {
    this(0);
  }
  public Semaphore(int i) {
    if (i < 0) throw new IllegalArgumentException(i + " < 0");
    counter = i;
  }
  /**
   * Increments internal counter, possibly awakening a thread
   * wait()ing in acquire().
   */
  public synchronized void release() {
    counter++;
    this.notify ();
  }
  /**
   * Decrements internal counter, blocking if the counter is already
   * zero.
   *
   * @exception InterruptedException passed from this.wait().
   */
  public synchronized void acquire() throws InterruptedException {
    while (counter == 0) {
      this.wait();
    }
    counter--;
  }
}