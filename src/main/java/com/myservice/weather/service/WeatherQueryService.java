package com.myservice.weather.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.myservice.weather.domain.AtmosphericInformation;
import com.myservice.weather.errorhandling.WeatherException;

public interface WeatherQueryService {

	
	  public Map<String, Object> ping();
	  public List<AtmosphericInformation>  weather(String iata, double radius ) throws WeatherException ;

	  public List<AtmosphericInformation> getRainyAtmosphericInfoList() throws WeatherException;
}
