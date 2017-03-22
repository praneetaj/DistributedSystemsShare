/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caching;
import static caching.Cache.CACHE_CAPACITY;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ian Gortan
 */
// Simple threadsafe class that uses a FIFO algorithm to evict kets from the cache
//
public class EvictionList {
    
    private int front = 0;
    private int victim= 0;
    private int numEntries = 0;
    
    // list is maintained as a circular FIFO so that when teh cache reaches capacity, the
    // element pointed to by victim is removed from the cache before storing tne replacement
    // key
    private String[] list = new String [CACHE_CAPACITY];
    private ConcurrentHashMap<String, String> cache;
    
    public EvictionList(ConcurrentHashMap<String, String> store) {
        cache = store;
    }
    
    synchronized public void addKey (String key) {
        
        String keyToEvict ; 
        
        if (numEntries == CACHE_CAPACITY) {
            // cache is full so we need to evict an entry thatt 
            // has been there for longest
            keyToEvict = list [victim];
            cache.remove(keyToEvict);
            System.out.println("Evicted key " + keyToEvict);
            list [front] = key;
            front = (front + 1) % CACHE_CAPACITY;
            victim = (victim + 1 ) % CACHE_CAPACITY;
        } else {
            // still space in cache, add entry to key list
            list [front] = key;
            front = (front + 1) % CACHE_CAPACITY;
            numEntries++;
        }
    
    }
}
