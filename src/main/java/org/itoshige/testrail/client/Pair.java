package org.itoshige.testrail.client;

/**
 * Tuple
 * 
 * @author itoshige
 * 
 * @param <F>
 * @param <S>
 */
public class Pair<F, S> {
    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        Pair pair = (Pair) obj;
        return (first.equals(pair.getFirst()) && second.equals(pair.getSecond()));
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }
}
