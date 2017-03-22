/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caching;
import static caching.CacheServerThread.REQUEST_LEN;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Ian Gortan
 */
public class CacheThread extends Thread {
 
  private ConcurrentHashMap<String,String> cache; 
  private int size;
    private HitsAndMisses hams;
  private String serverHostName = null;
  private EvictionList evict;
  Socket clientSocket;
    
  public CacheThread(Socket s, ConcurrentHashMap<String, String> store, EvictionList ev, int capacity, HitsAndMisses hams) {
    clientSocket = s;
    cache = store ;
    size = capacity;
    evict = ev;
      this.hams = hams;
  }
    
    public void run() {
        boolean running = true;
        
        try {
            // etsablish socket to server
            Socket s = new Socket(serverHostName, 12031); // TO DO make configuarble from command line
            PrintWriter toServer =
                    new PrintWriter(s.getOutputStream(), true);
            BufferedReader fromServer =
                    new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
            // establish connection to client
            BufferedReader   in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter   out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            // get request from client - must be a 'get' or "end"  for now
            while ( running ) {
                String request = in.readLine();

                String[] command = request.split("\\s+", REQUEST_LEN);
                String cacheValue;
                switch (command[0]) {
                    case "get":  
                        // System.out.println("Request is get :" + command[1]);
                        if (cache.containsKey(command[1])) {
                            // System.out.println("found the baby in the cache");
                            cacheValue = cache.get(command[1]);
                            hams.incrementHits();
                        } else {
                            // get connection to server and get requested value
                            toServer.println(request);
                            // get the new value from server and store in cache
                            cacheValue = fromServer.readLine();  
                            cache.put(command[1], cacheValue);
                            evict.addKey(command[1]);
                           hams.incrementMisses();
                        }

                        out.println(cacheValue);
                        break;
                    case "end":
                        toServer.println(request);
                        running = false;

                    default:
                        out.println("error - did nuffin");
                    }

                out.flush();    
                // System.out.println("Reply sent");
            }
      
        } catch (Exception e) {
               e.printStackTrace();
        }
    }
}
