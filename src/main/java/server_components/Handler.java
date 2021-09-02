package server_components;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Handler implements Runnable {
  protected Socket clientSocket;
  protected BufferedReader input;
  protected PrintWriter output;

  public abstract void freeCachedQueries();
}
