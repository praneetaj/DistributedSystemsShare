package consistenthashing;

import java.util.Arrays;

/**
 * Created by Praneeta on 10/25/2016.
 */
public class ConsistentHashClient {
    private static final int NUM_REPLICAS = 3;
    private static final String[] NODES = {
            "Bharat",
            "Praneeta",
            "Bob",
            "Johny",
            "Ari",
            "Northeastern",
            "Boston",
            "Seattle",
            "India",
            "Canada"
    };

    public static void main(String[] args) {
        System.out.println("starting ....");
        HashFunction<TupleGenerator> t = new HashFunction<>();
        ConsistentHash<String> hash = new ConsistentHash<>(t, NUM_REPLICAS, Arrays.asList(NODES));
        for (int i = 0; i < 100; i++) {
            hash.add("string" + i + Math.random());
        }
        hash.toStringRep();
    }
}
