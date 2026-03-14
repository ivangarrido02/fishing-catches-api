package com.ivandev.registrocapturas.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PatchLogger {

	public static Map<String, Object> getNonNullFields(Object dto) {
		Map<String, Object> map = new HashMap<>();
		Arrays.stream(dto.getClass().getDeclaredFields())
			.forEach(f -> {f.setAccessible(true);
			try {
				Object value = f.get(dto);
				if (value != null) {
					map.put(f.getName(), value);
				}
			}catch(IllegalAccessException ignored) {}
		});
		return map;
	}
}
