package com.bulut.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bulut.api.constans.Constants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

@Service
public class AddParams2RedisService {
	private static JedisPool pool = null;
	private String applicationNam;

	public void addValues2RedisCache(List<Map<String, Object>> dataList, String applicationName) {
		this.applicationNam = applicationName;
		Jedis jedis = null;
		try {
			pool = new JedisPool(Constants.REDIS_HOST, Constants.REDIS_PORT);
			jedis = pool.getResource();
			Map<String, String> objectMap = new HashMap<>();
			for (Map<String, Object> map : dataList) {

				objectMap.put((String) map.get("name"), (String) map.get("value"));

			}
			jedis.hmset(applicationName, objectMap);
		} catch (JedisException e) {
			if (null != jedis) {
				pool.returnBrokenResource(jedis);
				jedis = null;
			}
		} finally {
			if (null != jedis)
				pool.returnResource(jedis);
		}

	}

	public String getValueFromRedis(String key) {
		Jedis jedis = null;
		pool = new JedisPool(Constants.REDIS_HOST, Constants.REDIS_PORT);
		jedis = pool.getResource();

		Map<String, String> retrieveMap = jedis.hgetAll(applicationNam);

		return retrieveMap.get(key);

	}

}
