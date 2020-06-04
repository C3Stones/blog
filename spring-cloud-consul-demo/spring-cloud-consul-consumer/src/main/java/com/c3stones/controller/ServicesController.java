package com.c3stones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 服务Controller
 * 
 * @author CL
 *
 */
@RestController
public class ServicesController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	/**
	 * 获得所以服务列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getAllServices")
	public List<ServiceInstance> getAllServices() {
		return discoveryClient.getInstances("service-producer");
	}

	/**
	 * 获取服务Uri
	 * 
	 * @return
	 */
	@RequestMapping("/getServiceUri")
	public String getServiceUri() {
		return loadBalancerClient.choose("service-producer").getUri().toString();
	}

	/**
	 * 负载均衡调用提供者接口
	 * 
	 * @return
	 */
	@RequestMapping("/loadBalancer")
	public String call() {
		ServiceInstance serviceInstance = loadBalancerClient.choose("service-producer");
		return new RestTemplate().getForObject(serviceInstance.getUri().toString() + "/get", String.class);
	}
}
