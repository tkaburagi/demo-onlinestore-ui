package com.example.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.entity.Product;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class ServiceClient {

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	@Autowired
	DiscoveryClient discoveryClient;

	@HystrixCommand
	public Product[] getProducts() throws JsonParseException, JsonMappingException, IOException {
		InstanceInfo instanceInfo = discoveryClient.getNextServerFromEureka("ONLINESTORE-SERVICE", false);
		restTemplate = new RestTemplate();
		String targetUrl = UriComponentsBuilder.fromUriString(instanceInfo.getHomePageUrl()).path("/").build().toString();
		String result = restTemplate.getForObject(targetUrl, String.class);
		ObjectMapper mapper = new ObjectMapper();
		Product[] prd = mapper.readValue(result, Product[].class);
		return prd;
	}
	
	@HystrixCommand
	public Product[] getProductsByName(String name) throws JsonParseException, JsonMappingException, IOException {
		InstanceInfo instanceInfo = discoveryClient.getNextServerFromEureka("ONLINESTORE-SERVICE", false);
		restTemplate = new RestTemplate();
		String targetUrl = UriComponentsBuilder.fromUriString(instanceInfo.getHomePageUrl()).path("/search").queryParam("name", name).build().toString();
		String result = restTemplate.getForObject(targetUrl, String.class);
		ObjectMapper mapper = new ObjectMapper();
		Product[] prd = mapper.readValue(result, Product[].class);
		return prd;
	}
	
	@HystrixCommand(fallbackMethod = "fallbackGetInfo")
	public String[] getLocalInfo() {
		InstanceInfo info = discoveryClient.getNextServerFromEureka("ONLINESTORE-SERVICE", false);
		String targetUrl = UriComponentsBuilder.fromUriString(info.getHomePageUrl()).path("/getlocalinfo").build().toString();
		String[] resultFromService = restTemplate.getForObject(targetUrl, String[].class);
		return resultFromService;
	}

	@SuppressWarnings("unused")
	private String[]  fallbackGetInfo() {
		String[] list = new String[3];
		list[0] = "-";
		list[1] = "-";
		list[2] = "-";
		return list;
	}
	
	public String kill() {
		InstanceInfo info = discoveryClient.getNextServerFromEureka("ONLINESTORE-SERVICE", false);
		String targetUrl = UriComponentsBuilder.fromUriString(info.getHomePageUrl()).path("/kill").build().toString();
		String resultFromService = restTemplate.getForObject(targetUrl, String.class);
		return resultFromService;
	}
}
