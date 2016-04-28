/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.util;

/**
 *
 * @author Aale
 * @param <B>
 */
public class MyPair<A, B> {

    private A first;
    private B second;

    /**
     *
     * @param first
     * @param second
     */
    public MyPair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof MyPair) {
            MyPair otherPair = (MyPair) other;
            return ((this.first == otherPair.first
                    || (this.first != null && otherPair.first != null
                    && this.first.equals(otherPair.first)))
                    && (this.second == otherPair.second
                    || (this.second != null && otherPair.second != null
                    && this.second.equals(otherPair.second))));
        }

        return false;
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    /**
     *
     * @return
     */
    public A getFirst() {
        return first;
    }

    /**
     *
     * @param first
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     *
     * @return
     */
    public B getSecond() {
        return second;
    }

    /**
     *
     * @param second
     */
    public void setSecond(B second) {
        this.second = second;
    }
}
