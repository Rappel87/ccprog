package hangman;

import java.net.Socket;

import reactor.WorkerThread;
import reactorapi.EventHandler;
import reactorapi.Handle;

public class AcceptHandler implements EventHandler <Socket>
{
  Handle <Socket> handle;
  Hangmanserver server;

  public AcceptHandler (Handle <Socket> handle, Hangmanserver server)
  {
    this.handle = handle;
    this.server = server;
  }
  @Override
  public Handle<Socket> getHandle ()
  {
    return this.handle;
  }

  @Override
  public void handleEvent (Socket s)
  {
    /* Create a new handler for handling the client data from the client socket */
    TCPTextHandler eh = new TCPTextHandler (new TCPTextHandle (s), this.server);
    /* Let the hangmanserver add the handler to the dispatcher */
    server.addHandler (eh);
  }

}
