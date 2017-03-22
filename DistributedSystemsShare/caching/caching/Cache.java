/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caching;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Ian Gortan
 */
public class Cache {
    final static int CACHE_CAPACITY = 1000;
    
    public static void main(String[] args) throws Exception {
        ConcurrentHashMap<String,String> store=new ConcurrentHashMap<String,String>(CACHE_CAPACITY); 
        EvictionList evict = new EvictionList(store);
        HitsAndMisses hams = new HitsAndMisses(0, 0);
        ServerSocket m_ServerSocket = new ServerSocket(12032);
        int count = 0;
        System.out.println("Cache started .....");
        while (true) {
            Socket clientSocket = m_ServerSocket.accept();
            CacheThread server = new CacheThread (clientSocket, store, evict, CACHE_CAPACITY, hams);
            server.start();
            count++;
            if (count == 30)
                break;
    }
    Thread.sleep(5000);
        System.out.println("hits: " + hams.hits);
        System.out.println("misses: " + hams.misses);
  }
}
