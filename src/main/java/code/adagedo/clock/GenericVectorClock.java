package code.adagedo.clock;


import net.jcip.annotations.Immutable;

import java.util.*;
import java.util.function.Supplier;

@Immutable
public class GenericVectorClock<N, V extends Version<V>> {

    private final N ref;
    private final Map<N, V> versions;
    private final Supplier<Map<N, V>> mapSupplier;

    private String lazyToString;

    public GenericVectorClock(N ref, Map<N, V> versions, Supplier<Map<N, V>> mapSupplier) {
        this.ref = Constraints.checkNotNull(ref);
        this.mapSupplier = Constraints.checkNotNull(mapSupplier);

        final Map<N, V> map = mapSupplier.get();
        map.putAll(versions);
        this.versions = Collections.unmodifiableMap(map);
    }

    public static <Nd, Ver extends  Version<Ver>> GenericVectorClock<Nd, Ver> of(final Nd node, final Ver version) throws NullPointerException{

        return GenericVectorClock.of(node, version, HashMap::new);
    }

    private static  <Nd, Ver extends  Version<Ver>> GenericVectorClock<Nd, Ver> of(Nd node, Ver version, final Supplier<Map<Nd, Ver>> mapSupplier) throws NullPointerException{
        Constraints.checkNotNull(node);
        Constraints.checkNotNull(version);
        Constraints.checkNotNull(mapSupplier);

        final Map<Nd, Ver> versions = mapSupplier.get();
        versions.put(node, version);

        return new GenericVectorClock<>(node, versions, mapSupplier);
    }

    public GenericVectorClock<N, V> add(final GenericVectorClock<N, V> other) throws NullPointerException, IllegalAccessError{
        Constraints.checkNotNull(other);
        Constraints.validateArgs(!other.ref.equals(ref));

        final  Map<N, V> versions = mapSupplier.get();
        versions.putAll(this.versions);

        final V version = other.version();
        versions.merge(other.ref, version, (n, v) -> version.next(v));
        versions.put(ref, version().next());
        return new GenericVectorClock<N, V>(ref, versions, mapSupplier);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        @SuppressWarnings("rawtypes")
        final GenericVectorClock other = (GenericVectorClock) object;
        return ref.equals(other.ref) && versions.equals(other.versions);
    }
    @Override
    public int hashCode() {
        return Objects.hash(ref, versions);
    }
    public GenericVectorClock<N, V> next() {
        final Map<N, V> versions = mapSupplier.get();
        versions.putAll(this.versions);
        versions.put(ref, versions.get(ref).next());
        return new GenericVectorClock<N, V>(ref, versions, mapSupplier);
    }


    public int size() {
        return versions.size();
    }

    @Override
    public String toString() {
        if (lazyToString == null) {
            final StringBuilder formatted = new StringBuilder("[");
            versions.forEach((k, v) -> formatted.append(k).append(":").append(v).append(","));
            formatted.setCharAt(formatted.length() - 1, ']');
            lazyToString = formatted.toString();
        }

        return lazyToString;
    }
    public V version() {
        return versions.get(ref);
    }
    public Optional<V> version(final Node node) {
        return Optional.ofNullable(versions.get(node));
    }
}
