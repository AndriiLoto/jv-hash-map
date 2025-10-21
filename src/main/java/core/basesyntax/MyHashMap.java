package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node<K, V>[] table;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key) & (table.length - 1);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            table[index] = new Node<>(key, value, table[index]);
            size++;
        }

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int localHash = hash(key);
        int index = localHash & (table.length - 1);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int newCapacity = (oldCapacity == 0) ? DEFAULT_CAPACITY : oldCapacity * 2;
        int newThreshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        if (oldTable != null) {
            for (Node<K, V> kvNode : oldTable) {
                while (kvNode != null) {
                    Node<K, V> next = kvNode.next;
                    int newIndex = hash(kvNode.key) & (newCapacity - 1);
                    kvNode.next = newTable[newIndex];
                    newTable[newIndex] = kvNode;
                    kvNode = next;
                }
            }
        }
        table = newTable;
        threshold = newThreshold;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
