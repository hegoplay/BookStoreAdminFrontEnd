package com.iuh.util;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionUtil {
	public static final String URL = "http://localhost:8080/bookstore";
	
	public static boolean isAdmin(String token) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
//		log.info("JWT: {}", response.getBody().getData().getToken());
		HttpEntity<Void> request = new HttpEntity<>(headers);
		ResponseEntity<Map> response = restTemplate.exchange(
                ConnectionUtil.URL + "/users/me",
                HttpMethod.GET,
                request,
                Map.class);
		List<Map<String,Object>> list = (List<Map<String, Object>>) ((Map<String,Object>) response.getBody().get("data")).get("roles");
		for (Map<String, Object> map : list) {
			log.info("Role: {}", map.get("name"));
			if (map.get("name").equals("ADMIN")) {
				return true;
			}
		}
		return false;
		
	}
}
