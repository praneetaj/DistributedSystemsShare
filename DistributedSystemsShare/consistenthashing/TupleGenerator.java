package consistenthashing;

/**
 * Created by Praneeta on 10/6/2016.
 */
public class TupleGenerator {
    private static int num1, num2;

    public TupleGenerator() {
        num1 = 0;
        num2 = 10000;
    }

    public TupleGenerator(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    synchronized public TupleGenerator getNextTuple () {
        return new TupleGenerator(++num1, ++num2);
    }
}
