package hangman;

import reactorapi.EventHandler;
import reactorapi.Handle;

public class TCPTextHandler implements EventHandler <String>
{
  
  Hangmanserver server;
  Handle<String> handle;


  public TCPTextHandler (Handle <String> handle, Hangmanserver server)
  {
    this.handle = handle;
    this.server = server;
  }

  @Override
  public Handle<String> getHandle ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void handleEvent (String s)
  {
    // TODO Auto-generated method stub

  }

}
