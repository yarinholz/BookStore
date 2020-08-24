package bgu.spl.mics;

public class Pair<F,S> {
    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    /**
     * Retrieves the right object in the pait.
     * <p>
     * @return s - the right object of the pair.
     */
    public S getSecond() {
        return second;
    }
    /**
     * set the the right object of the pair.
     * <p>
     * @param  second - the right object of the pair.
     */
    public void setSecond(S second) {
        this.second = second;
    }
    /**
     * Retrieves the left object in the pait.
     * <p>
     * @return F - the left object of the pair.
     */
    public F getFirst() {
        return first;
    }
    /**
     * set the the left object of the pair.
     * <p>
     * @param  first - the left object of the pair.
     */
    public void setFirst(F first) {
        this.first = first;
    }
}
