package caching;

/**
 * Created by Praneeta on 10/4/2016.
 */
public class HitsAndMisses {
    int hits;
    int misses;

    public HitsAndMisses(int hits, int misses) {
        this.hits = hits;
        this.misses = misses;
    }

    synchronized public void incrementHits() {
        hits++;
    }

    synchronized public void incrementMisses() {
        misses++;
    }
}
