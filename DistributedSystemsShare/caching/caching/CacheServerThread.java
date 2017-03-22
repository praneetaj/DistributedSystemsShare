/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian Gortan
 */
package caching;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

//
// Acceptsa series if get or put messages from a client
// Request stream is terminated by an 'end' message
//

class CacheServerThread extends Thread {
  private Socket clientSocket;
  private boolean running = true;
  private ConcurrentHashMap<String, String> data;
  final static int REQUEST_LEN = 3;

  CacheServerThread(Socket s, ConcurrentHashMap<String, String> store) {
    clientSocket = s;
    data = store ;
  }

  public void run() {

    //System.out.println("Accepted Client: Address - "
      //   + clientSocket.getInetAddress().getHostName());
    try {
      BufferedReader   in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      PrintWriter   out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
      boolean running = true;
      
      while (running) {
            // get request
            String request = in.readLine();
            
            
            String[] command = request.split("\\s+", REQUEST_LEN);
            String result;
            switch (command[0]) {
                case "get":  
                    // System.out.println("Request is get :" + command[1]);
                    if (data.containsKey(command[1])) {
                        result = data.get(command[1]);
                    } else {
                        result = "empty slot";
                    }

                    out.println(result);
                    break;
                case "put": 
                    // System.out.println("Request is put :" + command[1] + command[2]);
                    if (data.containsKey(command[1])) {
                        data.replace(command[1], command[2]);
                    } else {
                        data.put(command[1], command[2]);
                    }
                    
                    out.println("success");
                    break;
                case "end":
                    running =false;
                default:
                    out.println("error - incoreect command");
             }
            
            out.flush();    
            
           
      }
      System.out.println("end received");
      
    } catch (Exception e) {
           e.printStackTrace();
    }

      System.out.println("Thread exiting");
  }
  
} //end
