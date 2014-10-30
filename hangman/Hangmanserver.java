package hangman;
import java.net.Socket;

import hangmanrules.HangmanRules;
import reactor.*;
import reactorapi.EventHandler;

public class Hangmanserver
{
  private Dispatcher d;
  private HangmanRules<Socket> rules;
  
  
  public Hangmanserver ()
  {
    ;
  }
  
  
  public <T> void addHandler (EventHandler<T> eh)
  {
    WorkerThread<T> thread = new WorkerThread<T> (eh, d.getQueue());
    d.addHandler (eh);
    d.addHandlerThreadMapping (eh, thread);
  }
  
  public void removeHandler (EventHandler<?> eh)
  {
    ;
  }
}
