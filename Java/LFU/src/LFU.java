/**
 * Created by salman on 17/7/17.
 */
/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;

class LFUCache<K, V> {

    private HashMap<K, Integer> keyFreqMap;
    private HashMap<Integer, HashMap<K, V> > freqHashMap;
    int capacity;
    int _size;
    int minFreq;

    public LFUCache(int capacity) {
        keyFreqMap = new HashMap<K, Integer>();
        freqHashMap = new HashMap<Integer, HashMap<K, V> >();
        _size = 0;
        minFreq = 1;
        this.capacity = capacity;
    }

    public boolean containsKey(K key){
        return keyFreqMap.containsKey(key);
    }

    public V get(K key){
        // If doesn't contain key, return null
        if(!containsKey(key)) return null;
        // Get freq of key
        int freq = keyFreqMap.get(key);
        // Get value of key
        V value = freqHashMap.get(freq).get(key);
        // Remove key, value from freqHashMap[freq]
        freqHashMap.get(freq).remove(key);
        if(freqHashMap.get(freq).size() == 0){
            freqHashMap.remove(freq);
            if(freq == minFreq) minFreq++;
        }
        // Add key, value to freqHashMap[freq+1]
        freq++;
        if(!freqHashMap.containsKey(freq))
            freqHashMap.put(freq, new HashMap<K, V>());
        freqHashMap.get(freq).put(key, value);
        // Update frequency of key
        keyFreqMap.put(key, freq);
        // Return value
        return value;
    }

    public void evict(){
        // Get key from minFreq
        if(_size == 0) return;
        Map.Entry<K, V> entry = freqHashMap
                .get(minFreq)
                .entrySet()
                .iterator()
                .next();
        K minFreqKey = entry.getKey();
        // Remove key from freqHashMap
        freqHashMap.get(minFreq).remove(minFreqKey);
        if(freqHashMap.get(minFreq).size() == 0){
            freqHashMap.remove(minFreq);
            minFreq++;
        }
        // Remove key from keyFreqMap
        keyFreqMap.remove(minFreqKey);
        // Update size
        _size--;
    }

    public void set(K key, V value){
        // Check if already present
        if(containsKey(key)){
            // Then update the value
            get(key);
            int freq = keyFreqMap.get(key);
            freqHashMap.get(freq).put(key, value);
            return;
        }
        // Check need for eviction
        if(_size == capacity)
            evict();
        // Add key, value to frequncy = 1
        int freq = minFreq = 1;
        if(!freqHashMap.containsKey(freq))
            freqHashMap.put(freq, new HashMap<K, V>());
        freqHashMap.get(freq).put(key, value);
        keyFreqMap.put(key, freq);
        // Update size
        _size++;
    }

    public int size(){
        return _size;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void printLFU(){
        for(Map.Entry<Integer, HashMap<K, V> > entry: freqHashMap.entrySet()){
            System.out.print(String.valueOf(entry.getKey()) + " : ");
            for(Map.Entry<K, V> kv: entry.getValue().entrySet())
                System.out.print("(" + kv.getKey() + ", " + kv.getValue() + ") ");
            System.out.print("\n");
        }
    }

}

class LFU
{
    public static void main (String[] args) throws java.lang.Exception
    {
        // your code goes here
        LFUCache<Integer, Integer> lfu = new LFUCache<>(100);
        lfu.set(5, 5);
        lfu.set(4, 4);
        lfu.set(3, 3);
        lfu.set(4, 4);
        System.out.println(lfu.get(5));
        lfu.printLFU();
        lfu.evict();
        lfu.printLFU();
        lfu.evict();
        lfu.printLFU();
    }
}