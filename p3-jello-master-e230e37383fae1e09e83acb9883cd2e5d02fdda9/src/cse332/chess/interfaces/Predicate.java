package cse332.chess.interfaces;

/**
 * @author Owen Durni (opd@andrew.cmu.edu)
 *
 *         A functor that serves as a predicate for some type.
 */
public interface Predicate<T> {
    public boolean check(T t);
}
