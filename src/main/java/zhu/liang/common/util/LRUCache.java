package zhu.liang.common.util;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 用LinkedHashMap 实现的LRU 存储，通过 ReentrantLock 进行并发控制
 * @author zhengmingzhi
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final float hashTableLoadFactor = 0.75f;
    private static final int DEFAULT_CAPACITY = 100;
    protected Map<K, ValueEntry> map;
    private final Lock lock = new ReentrantLock();
    private final transient int maxCapacity;
    private int MINI_ACCESS = 10;
    private boolean removeAllOverTime = false;
    private int overTimeSeconds = 60;

    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }
    public LRUCache(int capacity,boolean removeAllOverTime,int overTimeSeconds) {
        if (capacity <= 0)
            capacity = DEFAULT_CAPACITY;
        this.maxCapacity = capacity;
        this.removeAllOverTime = removeAllOverTime;
        this.overTimeSeconds = overTimeSeconds;
        int hashTableCapacity = (int) Math.ceil(this.maxCapacity / hashTableLoadFactor) + 1;
        this.map =  new LinkedHashMap<K, ValueEntry>(hashTableCapacity, hashTableLoadFactor,true);
//        this.map = new HashMap<K, ValueEntry>(maxCapacity);
    }
    public LRUCache(int capacity) {
        if (capacity <= 0)
            throw new RuntimeException("缓存容量不得小于0");
        this.maxCapacity = capacity;
//        this.map = new HashMap<K, ValueEntry>(maxCapacity);

        int hashTableCapacity = (int) Math.ceil(this.maxCapacity / hashTableLoadFactor) + 1;
        map = new LinkedHashMap<K, ValueEntry>(hashTableCapacity, hashTableLoadFactor,
                true) {
            // (an anonymous inner class)
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, ValueEntry> eldest) {
                return size() > LRUCache.this.maxCapacity;
            }
        };
    }
    public boolean containsKey(K key) {
        try {
            lock.lock();
            return this.map.containsKey(key);
        } finally {
            lock.unlock();
        }
    }
    public V put(K key, V value) {
    	if (key == null || value == null) {
    		throw new NullPointerException("key == null || value == null");
    	}
        try {
            lock.lock();
            if ((map.size() > maxCapacity - 1) && !map.containsKey(key)) {
                Set<Map.Entry<K, ValueEntry>> entries = this.map.entrySet();
                removeRencentlyLeastAccess(entries);
            }
            ValueEntry valueEntry = map.put(key, new ValueEntry(value));
            if (valueEntry != null)
                return valueEntry.value;
            else
                return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 移除最近最少访问
     */
    protected void removeRencentlyLeastAccess(
            Set<Map.Entry<K, ValueEntry>> entries) {
        // 最小使用次数
        int least = 0;
        // 最久没有被访问
        long earliest = 0;
        List<K> moreThanOneMins = new ArrayList<>();
        K toBeRemovedByCount = null;
        K toBeRemovedByTime = null;
        Iterator<Map.Entry<K, ValueEntry>> it = entries.iterator();
        // 取出第一个作为要移除对象
        if (it.hasNext()) {
            Map.Entry<K, ValueEntry> valueEntry = it.next();
            least = valueEntry.getValue().count.get();
            toBeRemovedByCount = valueEntry.getKey();
            earliest = valueEntry.getValue().lastAccess.get();
            toBeRemovedByTime = valueEntry.getKey();
        }
        //找出时间最短和使用次数最少的key
        while (it.hasNext()) {
            Map.Entry<K, ValueEntry> valueEntry = it.next();
            if (valueEntry.getValue().count.get() < least) {
                least = valueEntry.getValue().count.get();
                toBeRemovedByCount = valueEntry.getKey();
            }
            //移除超过设置超时时间overTimeSeconds的key
            if(removeAllOverTime && (System.nanoTime() - valueEntry.getValue().lastAccess.get() >= overTimeSeconds*1000*1000*1000)){
                moreThanOneMins.add(valueEntry.getKey());
            }
            if (valueEntry.getValue().lastAccess.get() < earliest) {
                earliest = valueEntry.getValue().count.get();
                toBeRemovedByTime = valueEntry.getKey();
            }
        }
        // 如果最少使用次数大于 MINI_ACCESS，那么移除访问时间最早的项(也就是最久没有被访问的项）
        if (least > MINI_ACCESS) {
            map.remove(toBeRemovedByTime);
        } else {
            map.remove(toBeRemovedByCount);
        }
        if(moreThanOneMins.size() > 0)
            for(K k : moreThanOneMins){
                map.remove(k);
            }
    }

    public V get(K key) {
        try {
            lock.lock();
            V value = null;
            ValueEntry valueEntry = map.get(key);
            if (valueEntry != null) {
                // 更新访问时间戳
                valueEntry.updateLastAccess();
                // 更新访问次数
                valueEntry.count.incrementAndGet();
                value = valueEntry.value;
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        try {
            lock.lock();
            map.clear();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        try {
            lock.lock();
            return map.size();
        } finally {
            lock.unlock();
        }
    }

    public Collection<Map.Entry<K, V>> getAll() {
        try {
            lock.lock();
            Set<K> keys = map.keySet();
            Map<K, V> tmp = new HashMap<K, V>();
            for (K key : keys) {
                tmp.put(key, map.get(key).value);
            }
            return new ArrayList<Map.Entry<K, V>>(tmp.entrySet());
        } finally {
            lock.unlock();
        }
    }

    class ValueEntry implements Serializable {
        private static final long serialVersionUID = -2154615706795396035L;

        private V value;

        private AtomicInteger count;

        private AtomicLong lastAccess;

        public ValueEntry(V value) {
            this.value = value;
            this.count = new AtomicInteger(0);
            lastAccess = new AtomicLong(System.nanoTime());
        }

        public void updateLastAccess() {
            this.lastAccess.set(System.nanoTime());
        }

    }
    public static void main(String[] args) {
		System.out.println(4>>3);
		System.out.println(4>>>3);
		System.out.println(1<<4);
	}
}
