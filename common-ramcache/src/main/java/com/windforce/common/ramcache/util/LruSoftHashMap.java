package com.windforce.common.ramcache.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LruSoftHashMap<K, V> {
	private Map<K, Reference<V>> cacheMap = new HashMap<K, Reference<V>>();
	private final Map<K, V> hardCache;
	private final ReferenceQueue<V> refQueue = new ReferenceQueue<V>();
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
	private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

	public LruSoftHashMap(Map<K, V> map) {
		if (map == null) {
			throw new NullPointerException("map is null Exception");
		}
		hardCache = map;
	}

	public int getCacheMapSize() {
		return cacheMap.size();
	}

	public V get(K key) {
		readLock.lock();
		try {
			V result = hardCache.get(key);
			if (result != null) {
				return result;
			}

			Reference<V> ref = cacheMap.get(key);
			if (ref != null) {
				result = ref.get();
				if (result == null) {
					cacheMap.remove(key);
				} else {
					hardCache.put(key, result);
				}
			}
			return result;
		} finally {
			readLock.unlock();
		}
	}

	public void put(K key, V value) {
		writeLock.lock();
		try {
			Reference<V> entry = new Entry(key, value, refQueue);
			cacheMap.put(key, entry);
			hardCache.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	public V remove(K key) {
		writeLock.lock();
		try {
			V result = null;
			result = hardCache.remove(key);
			if (result == null) {
				Reference<V> ref = cacheMap.remove(key);
				if (ref != null) {
					result = ref.get();
				}
			}
			return result;
		} finally {
			writeLock.unlock();
		}
	}

	private class Entry extends SoftReference<V> {
		private K key;

		Entry(K key, V referent, ReferenceQueue<? super V> q) {
			super(referent, q);
			this.key = key;
		}

		K getKey() {
			return key;
		}
	}

	public Collection<V> values() {
		return hardCache.values();
	}

}
