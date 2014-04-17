package com.pir.services.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Component;

@Component("cache")
public class ApplicationCacheService {

	public void put(String key, Object item) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("generalUseCache");
		cache.put(new Element(key,item));
	}
	
	public Object get(String key) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("generalUseCache");
		return cache.get(key).getObjectValue();
	}
	
	public void delete(String key) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("generalUseCache");
		cache.remove(key);
	}
	
	public void clear() {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("generalUseCache");
		cache.removeAll();
	}
}
