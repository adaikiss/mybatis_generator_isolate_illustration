/**
 * 
 */
package com.example.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * not found to default
 * 
 * @author hlw
 * 
 */
@SuppressWarnings("serial")
public class NotFoundToDefaultMap<K, V> extends HashMap<K, V> {
	private K defaultKey;
	public NotFoundToDefaultMap(Map<K, V> map, K defaultKey) {
		this.defaultKey = defaultKey;
		this.putAll(map);
	}

	@Override
	public V get(Object key) {
		V v = super.get(key);
		if (v == null) {
			return super.get(defaultKey);
		}
		return v;
	}

}
