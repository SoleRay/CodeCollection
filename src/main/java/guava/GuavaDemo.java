package guava;

import com.google.common.base.Stopwatch;
import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuavaDemo {

    public void createCommon(){
        ArrayList<String> list = Lists.newArrayList();
        Set<String> set = Sets.newHashSet();
    }

    public void createImmutable(){
        ImmutableList<String> list = ImmutableList.of("a", "b", "c");
        ImmutableSet<String> set = ImmutableSet.of("a", "b");
    }

    /**
     * // use java
     * Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
     */
    public void createMultimap(){
        Multimap<String, Integer> map = ArrayListMultimap.create();
        map.put("key1", 1);
        map.put("key1", 2);
        // [1, 2]
        System.out.println(map.get("key1"));


    }

    public void stopWatch(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        // do something
        long second = stopwatch.elapsed(TimeUnit.SECONDS);
    }
}
