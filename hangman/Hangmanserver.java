package hangman;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hangmanrules.HangmanRules;
import reactor.*;
import reactorapi.EventHandler;
import reactorapi.Handle;

public class Hangmanserver
{
  private Dispatcher disp;
  private HangmanRules<Socket> rules;
  
  
  public Hangmanserver (String wordToGuess, Integer numberOfTries)
  {
    disp = new Dispatcher ();
    rules = new HangmanRules<Socket> (wordToGuess, numberOfTries);
    
  }
  
  
  public <T> void addHandler (EventHandler<T> eh)
  {
    disp.addHandler (eh);
  }
  
  public <T> void removeHandler (EventHandler<T> eh)
  {
    disp.removeHandler (eh);
  }
  
  public void addNewPlayer (Socket s, String name)
  {
    this.rules.addNewPlayer (s, name);
  }
  
  public void guess (char guess, String name)
  {
    boolean finished = false;
    finished = rules.makeGuess (guess);
    
    writeMsgToPlayers (getGameState(guess, name));
    
    if (finished)
    {
      /* Start cleaning up */
      cleanup ();
    }
  }
  
  private void cleanup ()
  {
    WorkerThread<?> thread = null;
    Handle <?> handle      = null;
    HashMap<EventHandler <?>, WorkerThread <?>> map = null;
    
    map = disp.getMap ();
    
    for (EventHandler<?> eh: map.keySet ())
    {
      thread = map.get (eh);
      handle = eh.getHandle ();
      
      /* If it is the server-socket, then close it */
      if (handle instanceof AcceptHandle)
      {
        ((AcceptHandle) handle).close ();
      }
      /* If it is the client-socket, then close it */
      else
      {
        ((TCPTextHandle) handle).close ();
      }
      
      /* Cancel the thread and remove its handler from the dispatcher */
      disp.removeHandler (eh);
      thread.cancelThread ();
    }
  }

  public void writeMsgToPlayers (String msg)
  {
    Socket s = null;
    TCPTextHandle handle = null;
    List<HangmanRules<Socket>.Player> players = new ArrayList <HangmanRules<Socket>.Player>();
    
    /* Get all the clients, to send them a message */
    players = rules.getPlayers ();
    
    for (HangmanRules<Socket>.Player player : players)
    {
      /* The handle already has an write interface, so we don't need to bother */
      s = player.playerData;
      handle = new TCPTextHandle (s);
      
      handle.write (msg);
    }
  }
  
  public String getGameState (char guess, String name)
  {
    StringBuilder sb  = new StringBuilder ();
    int tries         = rules.getTriesLeft ();
    String maskedWord = rules.getMaskedWord ();
    
    sb.append (tries);
    sb.append (" ");
    sb.append (maskedWord);
    sb.append (" ");
    sb.append (tries);
    sb.append (" ");
    sb.append (name);
    
    return sb.toString ();
    
  }

  public static void main (String[] args)
  {
    AcceptHandle ahandle = null;
    AcceptHandler ah     = null;
    WorkerThread<Socket> thread = null;
    
    Hangmanserver server = new Hangmanserver ("Kindergarten", 10);
    
    try
    {
      ahandle = new AcceptHandle ();
    } catch (IOException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
    ah = new AcceptHandler (ahandle, server);
    
    /* add the new handler and store its mapping to its thread */
    server.disp.addHandler (ah);
    
    try
    {
      server.disp.handleEvents ();
    } catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
}
