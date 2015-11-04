package com.example.personalproject.networking;

import java.util.HashMap;
import java.util.Map;

public class MemoryCache {
	private static MemoryCache Instance = null;

	public static synchronized MemoryCache getInstance() {
		if (Instance == null)
			Instance = new MemoryCache();
		return Instance;
	}

	private Map<String, String> map = new HashMap<String, String>();

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void clearCachMap() {
		map.clear();
	}

	public void putValueInCache(String key, String value) {
		map.put(key, value);
	}

	public String getValueFromCache(String key) {
		return map.get(key);
	}

	public boolean checkIfCacheContainsKey(String key) {
		return map.containsKey(key);

	}
}
