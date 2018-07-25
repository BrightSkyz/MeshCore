package me.bsky.skycore.api.helpers;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BlockingMap<K, V> {
    private final Map<K, BlockingQueue<V>> map = new ConcurrentHashMap<>();

    private synchronized BlockingQueue<V> ensureQueueExists(K key) {
        //concurrentMap.putIfAbsent would require creating a new
        //blocking queue each time put or get is called
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            BlockingQueue<V> queue = new ArrayBlockingQueue<>(1);
            map.put(key, queue);
            return queue;
        }
    }

    public boolean put(K key, V value, long timeout, TimeUnit timeUnit) {
        BlockingQueue<V> queue = ensureQueueExists(key);
        try {
            return queue.offer(value, timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public V get(K key, long timeout, TimeUnit timeUnit) {
        BlockingQueue<V> queue = ensureQueueExists(key);
        try {
            return queue.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}