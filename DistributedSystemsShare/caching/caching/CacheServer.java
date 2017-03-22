package caching;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class CacheServer {
    
    final static int CAPACITY = 10000;
    int hits, misses;
    
    public static void main(String[] args) throws Exception {
        ConcurrentHashMap<String,String> store=new ConcurrentHashMap<String,String>(CAPACITY); 
        ServerSocket m_ServerSocket = new ServerSocket(12031);
        int id = 0;
        System.out.println("Server started .....");
        while (true) {
            Socket clientSocket = m_ServerSocket.accept();
            CacheServerThread server = new CacheServerThread (clientSocket, store);
            server.start();
     
    }
  }
}


  

