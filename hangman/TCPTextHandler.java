package hangman;

import reactorapi.EventHandler;
import reactorapi.Handle;

public class TCPTextHandler implements EventHandler <String>
{
  
  private Hangmanserver server;
  private TCPTextHandle handle;
  private boolean notFirst;
  private String name;


  public TCPTextHandler (TCPTextHandle handle, Hangmanserver server)
  {
    this.handle    = handle;
    this.server    = server;
    /* Indicates if the socket received its first message or not */
    this.notFirst  = false;
    this.name      = null;
  }

  @Override
  public Handle<String> getHandle ()
  {
    return handle;
  }

  @Override
  public void handleEvent (String s)
  {
    String trimmed = null;
    
    trimmed = s.trim ();
    
    if (notFirst)
    {
      server.guess (trimmed.charAt (0), name);
    }
    else
    {
      /* First time -> add new player */
      server.addNewPlayer (handle.getSocket(), trimmed);
      name = s;
      notFirst = true;
    }
  }

}
