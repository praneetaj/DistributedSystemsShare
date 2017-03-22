package consistenthashing;

/**
 * Created by Praneeta on 10/25/2016.
 */
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<T> {

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<BigInteger, T> circle =
            new TreeMap<BigInteger, T>();

    public ConsistentHash(HashFunction hashFunction,
                          int numberOfReplicas, Collection<T> nodes) {

        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;

        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hash(node.toString() + i),
                    node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hash(node.toString() + i));
        }
    }

    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        BigInteger hash = hashFunction.hash(key);
        if (!circle.containsKey(hash)) {
            SortedMap<BigInteger, T> tailMap =
                    circle.tailMap(hash);
            hash = tailMap.isEmpty() ?
                    circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public void toStringRep() {
        for (Map.Entry<BigInteger, T> e : circle.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }

}
