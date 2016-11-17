package com.myservice.weather.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.myservice.weather.errorhandling.WeatherExceptionMapper;
import com.myservice.weather.service.WeatherCollectorService;
import com.myservice.weather.service.WeatherQueryService;

/**
 * used to enable jersey bean to be injected by spring DI
 * @author matroush
 *
 */
public class JersySpringConfig extends ResourceConfig {

	public JersySpringConfig() {
		register(RequestContextFilter.class);
		register(WeatherQueryService.class);
		register(WeatherCollectorService.class);
		
		register(WeatherExceptionMapper.class);

	}
}
