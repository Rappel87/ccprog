package hangman;

import java.net.Socket;

import hangmanrules.HangmanRules;
import reactorapi.EventHandler;
import reactorapi.Handle;

public class TCPTextHandler implements EventHandler <String>
{

  private Hangmanserver server;
  private TCPTextHandle handle;
  private boolean notFirst;
  private HangmanRules<Socket>.Player player;


  public TCPTextHandler (TCPTextHandle handle, Hangmanserver server)
  {
    this.handle    = handle;
    this.server    = server;
    /* Indicates if the socket received its first message or not */
    this.notFirst  = false;
    this.player    = null;
  }

  @Override
  public Handle<String> getHandle ()
  {
    return handle;
  }

  public void removeMe() {
      server.removeHandler(this);
  }

  @Override
  public void handleEvent (String s)
  {
    // get a NULL event meaning that socket was closed by the client
    if (s == null) {
        server.removeHandler(this);
    }
    else {
        String trimmed = null;

        trimmed = s.trim ();

        if (notFirst)
        {
          server.guess (trimmed.charAt (0), player.name);
        }
        else
        {
          /* First time -> add new player */
          player = server.addNewPlayer (handle.getSocket(), trimmed);
          handle.write (server.getStatus ());
          notFirst = true;
        }
    }
  }
}
