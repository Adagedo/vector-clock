package code.adagedo.clock;

public interface Version<T> {

    T max(T t);

    T next();

    T next(T t);
}
