package com.cominatyou.util;

/**
 * A class that holds a pair of two different objects.
 */
public class Pair<T1, T2> {
    /**
     * The first object in the pair.
     */
    private final T1 first;

    /**
     * The second object in the pair.
     */
    private final T2 second;

    /**
     * Create a new pair.
     * @param first The first object in the pair.
     * @param second The second object in the pair.
     */
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first object in the pair.
     * @return The first object in the pair.
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * Get the second object in the pair.
     * @return
     */
    public T2 getSecond() {
        return second;
    }

    /**
     * Determine if this pair is equal to another object.
     * @param other The object to compare to.
     * @return {@code true} if the objects are equal, or {@code false} otherwise.
     */
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Pair)) return false;

        Pair<?, ?> otherPair = (Pair<?, ?>) other;
        return first.equals(otherPair.getFirst()) && second.equals(otherPair.getSecond());
    }
}
