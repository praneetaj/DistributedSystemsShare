/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caching;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

//
// Thread that sends a stream of get or out requests to a server, terminated by an 
// end message. AThreads behave as a thread group synchronizd by the CyclicBarrier
// 'synk'
//

public class CacheClientThread extends Thread{
   
    private String hostName;
    private int port;
    private CyclicBarrier synk;
    private String action;
    
    public CacheClientThread(String hostName, int port, CyclicBarrier barrier, String command) {
        this.hostName = hostName;
        this.port = port;
        synk = barrier;
        action = command;
    }
    
    public void run() {
        
        try {   
            // get connection to server
            Socket s = new Socket(hostName, port);
            PrintWriter out =
                    new PrintWriter(s.getOutputStream(), true);
                BufferedReader in =
                    new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
                
            // send out a sequence of requests    
            for (int i = 0; i < 1000 ; i++ ) {
                int value = ThreadLocalRandom.current().nextInt(1, 10000);
            
                out.println(action + " " +  Integer.toString(value) + " test");
                System.out.println(in.readLine());
                
            } 
            // close the server socket
            out.println("end" + " novalue"); 
            s.close();           
        
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            //System.exit(1);
        } 
        try {
           System.out.println("=====Thread waiting at barrier") ;
           synk.await();
           System.out.println("Thread finishing");
         } catch (InterruptedException ex) {
           return;
         } catch (BrokenBarrierException ex) {
           return;
         }
        
    }
}
