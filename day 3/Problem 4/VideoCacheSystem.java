import java.util.*;

class VideoCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public VideoCache(int capacity) {

        super(capacity, 0.75f, true);

        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {

        return size() > capacity;
    }
}

public class VideoCacheSystem {

    public static void main(String[] args) {

        VideoCache<Integer, String> cache =
                new VideoCache<>(3);

        cache.put(1, "Movie A");
        cache.put(2, "Movie B");
        cache.put(3, "Movie C");

        System.out.println("Initial Cache:");
        System.out.println(cache);

        cache.get(1);

        cache.put(4, "Movie D");

        System.out.println("\nCache After Accessing Key 1 and Adding Key 4:");
        System.out.println(cache);

        cache.get(3);

        cache.put(5, "Movie E");

        System.out.println("\nCache After Accessing Key 3 and Adding Key 5:");
        System.out.println(cache);
    }
}