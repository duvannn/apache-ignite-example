package jh.playground.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.util.Map;
import java.util.TreeMap;

public class TreeMapTest {
    private static class CustomKey implements Comparable<CustomKey> {
        private final int i;

        private CustomKey(int i) {
            this.i = i;
        }

        @Override
        public int compareTo(CustomKey o) {
            return Integer.compare(i, o.i);
        }
    }

    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start()) {
            Object o = ignite.binary().toBinary(createMap());
            System.out.println(o);
        }
    }

    private static Map<?, ?> createMap() {
        Map<CustomKey, String> m = new TreeMap<>();
        m.put(new CustomKey(1), "str1");
        return m;
    }
}
