package consistenthashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Praneeta on 10/25/2016.
 */
public class HashFunction<T> {

    public BigInteger hash(Object node) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashValue = md.digest(node.toString().getBytes());
            return new BigInteger(hashValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger("");
    }
}
