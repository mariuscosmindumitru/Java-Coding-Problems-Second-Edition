package modern.challenge;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface Streams<T> extends Stream<T> {   
   
    default Streams<T> removeAll(T... items) {
        return removeAll(Stream.of(items));
    }

    default Streams<T> removeAll(List<? extends T> items) {
        return removeAll(items.stream());
    }

    default Streams<T> removeAll(Stream<? extends T> items) {

        Set<? extends T> set = toSet(items);

        if (set.isEmpty()) {
            return this;
        }

        return filter(item -> !set.contains(item))
                .onClose(items::close);
    }       

    default Streams<T> retainAll(T... items) {
        return retainAll(Stream.of(items));
    }

    default Streams<T> retainAll(List<? extends T> items) {
        return retainAll(items.stream());
    }

    default Streams<T> retainAll(Stream<? extends T> items) {

        Set<? extends T> set = toSet(items);

        if (set.isEmpty()) {
            return from(Stream.empty());
        }

        return filter(item -> set.contains(item))
                .onClose(items::close);
    }
    
    static <T> Streams<T> from(Stream<? extends T> stream) {
        
        if (stream == null) {
            return from(Stream.empty());
        }    
                    
        if (stream instanceof Streams) {
            return (Streams<T>) stream;
        }

        return new StreamsWrapper<>(stream);
    }

    static <T> Set<T> toSet(Stream<? extends T> stream) {

        return stream.collect(Collectors.toSet());
    }
    
    @Override
    public Streams<T> filter(Predicate<? super T> predicate);
    
    @Override
    public <R> Streams<R> map(Function<? super T, ? extends R> mapper);
            
    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper);        
            
    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper);        
            
    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);        
            
    @Override
    public <R> Streams<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
    
    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
    
    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);
    
    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);
    
    @Override
    public Streams<T> distinct();
    
    @Override
    public Streams<T> sorted();
    
    @Override
    public Streams<T> sorted(Comparator<? super T> comparator);
    
    @Override
    public Streams<T> peek(Consumer<? super T> action);
    
    @Override
    public Streams<T> limit(long maxSize);
    
    @Override
    public Streams<T> skip(long n);
    
    @Override
    public void forEach(Consumer<? super T> action);
    
    @Override
    public void forEachOrdered(Consumer<? super T> action);
    
    @Override
    public Object[] toArray();
    
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator);
    
    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator);
    
    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator);
    
    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
    
    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner);
    
    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector);
    
    @Override
    public Optional<T> min(Comparator<? super T> comparator);
    
    @Override
    public Optional<T> max(Comparator<? super T> comparator);
    
    @Override
    public long count();
    
    @Override
    public boolean anyMatch(Predicate<? super T> predicate);
    
    @Override
    public boolean allMatch(Predicate<? super T> predicate);
    
    @Override
    public boolean noneMatch(Predicate<? super T> predicate);
    
    @Override
    public Optional<T> findFirst();

    @Override
    public List<T> toList();

    @Override
    public Streams<T> dropWhile(Predicate<? super T> predicate);

    @Override
    public Streams<T> takeWhile(Predicate<? super T> predicate);

    @Override
    public DoubleStream mapMultiToDouble(BiConsumer<? super T, ? super DoubleConsumer> mapper);

    @Override
    public LongStream mapMultiToLong(BiConsumer<? super T, ? super LongConsumer> mapper);

    @Override
    public IntStream mapMultiToInt(BiConsumer<? super T, ? super IntConsumer> mapper);

    @Override
    public <R> Streams<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> mapper);
           
    @Override
    public Optional<T> findAny();
    
    @Override
    public Iterator<T> iterator();
    
    @Override
    public Spliterator<T> spliterator();
    
    @Override
    public boolean isParallel();
    
    @Override
    public Streams<T> onClose(Runnable closeHandler);
    
    @Override
    public void close();
    
    @Override
    public Streams<T> parallel();
    
    @Override
    public Streams<T> unordered();
            
    @Override
    default Streams<T> sequential() {        
        return this;
    }
}