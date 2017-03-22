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

 
public class CacheClient {
    
    static CyclicBarrier barrier; 
    
    public static void main(String[] args)  {
        String hostName;
        final int MAX_PUT_THREADS = 10 ;
        final int MAX_GET_THREADS = 30 ;
        final int MAX_THREADS = MAX_PUT_THREADS + MAX_GET_THREADS;
        final int CACHE_SERVER_PORT = 12031;
        final int CACHE_PORT =12032;
        int port;
        
        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName= null;
            port = 12031;  // default port in SocketServer
        }
        barrier = new CyclicBarrier(MAX_THREADS+1);
        CacheClientThread clients[] = new CacheClientThread[MAX_THREADS];
        for (int i = 0; i < MAX_GET_THREADS ; i++) {
            clients[i] = new CacheClientThread (hostName, CACHE_PORT, barrier, "get");
        }   
        for (int i = 0; i < MAX_PUT_THREADS ; i++) {
            clients[i + MAX_GET_THREADS] = new CacheClientThread (hostName, CACHE_SERVER_PORT, barrier, "put");
        } 
        long start = System.currentTimeMillis();
        for (int i = 0; i < MAX_THREADS ; i++) {
            clients[i].start();
        }
        
        
        try {
           System.out.println("=====Main Thread waiting at barrier") ;
           barrier.await();
           System.out.println("Maim thread Thread finishing");
         } catch (InterruptedException ex) {
           return;
         } catch (BrokenBarrierException ex) {
           return;
         }
        
        long end = System.currentTimeMillis();
        System.out.println("Time  taken = " + (end - start) + "  milliseconds");
     
                
    }
}
