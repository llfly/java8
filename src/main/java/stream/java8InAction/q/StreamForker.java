package stream.java8InAction.q;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static stream.java8InAction.q.StreamForker.ForkingStreamConsumer.END_OF_STREAM;

/**
 * Created by fangyou on 2018/1/14.
 */
public class StreamForker<T> {
    private final Stream<T> stream;
    private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();

    public StreamForker(Stream<T> stream) {
        this.stream = stream;
    }


    // Function参数，它对流进行处理，将流转变为代表这些操作结果的任何类型
    // key参数，可以通过它取得操作的结果，并将这些键/函数对累积到一个内部的Map中
    public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
        forks.put(key, f);
        return this;
    }

    public static interface Results {
        public <R> R get(Object key);
    }


    public Results getResults() {
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer);
        } finally {
            consumer.finish();
        }
        return consumer;
    }

    private ForkingStreamConsumer<T> build() {
        List<BlockingQueue<T>> queues = new ArrayList<>();//  创建由队列组成的列表，每一个队列对应一个操作

        Map<Object, Future<?>> actions = forks.entrySet().stream().reduce( //  建立用于标识操作的键与包含操作结果的 Future 之间的映射关系
                new HashMap<Object, Future<?>>(),
                (map, e) -> {
                    map.put(e.getKey(), getOperationResult(queues, e.getValue()));
                    return map;
                },
                (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                }
        );

        return new ForkingStreamConsumer(queues, actions);
    }

    private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f) {
        BlockingQueue<T> queue = new LinkedBlockingQueue<>();
        queues.add(queue);
        Spliterator<T> spliterator =new BlockingQueueSpliterator<>(queue);
        Stream<T> source = StreamSupport.stream(spliterator, false);
        return CompletableFuture.supplyAsync( () -> f.apply(source) );
    }



    static class ForkingStreamConsumer<T> implements Consumer<T>, Results {
        private final List<BlockingQueue<T>> queues;
        static final Object END_OF_STREAM = new Object();
        private final Map<Object, Future<?>> actions;

        ForkingStreamConsumer(List<BlockingQueue<T>> queues,
                              Map<Object, Future<?>> actions) {
            this.queues = queues;
            this.actions = actions;
        }

        @Override
        public void accept(T t) {
            queues.forEach(q -> q.add(t));
        }

        void finish() {
            accept((T) END_OF_STREAM);
        }

        @Override
        public <R> R get(Object key) {
            try {
                return ((Future<R>) actions.get(key)).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    class BlockingQueueSpliterator<T> implements Spliterator<T> {
        private final BlockingQueue<T> q;

        BlockingQueueSpliterator(BlockingQueue<T> q) {
            this.q = q;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            T t;
            while (true) {
                try {
                    t = q.take();
                    break;
                } catch (InterruptedException e) {
                }
            }
            if (t != END_OF_STREAM) {
                action.accept(t);
                return true;
            }
            return false;
        }

        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return 0;
        }

        @Override
        public int characteristics() {
            return 0;
        }
    }


}