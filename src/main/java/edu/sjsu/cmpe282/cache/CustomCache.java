package edu.sjsu.cmpe282.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomCache {
	Map<String, ArrayList<String>> dataCache = Collections.synchronizedMap(new LinkedHashMap<String,ArrayList<String>>(20));

	public Map<String, ArrayList<String>> getDataCache() {
		return dataCache;
	}

	public void setDataCache(String name, ArrayList<String> value) {
		this.dataCache.put(name, value);
	}

}
