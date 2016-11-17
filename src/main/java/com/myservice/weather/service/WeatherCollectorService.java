package com.myservice.weather.service;

import java.util.Set;

import com.myservice.weather.domain.AirportData;
import com.myservice.weather.domain.DataPoint;
import com.myservice.weather.errorhandling.WeatherException;

public interface WeatherCollectorService {

	
	public Set<String> getAirports();
	public AirportData getAirport(String iata);
	public void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException;
	public void addAirport(String iataCode, String latString, String longString) throws WeatherException ;
	public void deleteAirportByCode(String iata) throws WeatherException;
	
	
}
