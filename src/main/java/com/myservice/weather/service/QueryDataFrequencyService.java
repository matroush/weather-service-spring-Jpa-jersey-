package com.myservice.weather.service;

import java.util.Map;
import java.util.Set;

public interface QueryDataFrequencyService {

	/****
	 * Get the Frequency of requested iata code
	 * @param airportsCodes
	 * @return Map key:code , Value is the Frequency
	 */
	public Map<String, Double> getRequestIataFrequency(Set<String> airportsCodes);

	/****
	 * Get the Frequency of requested radius
	 * @param airportsCodes
	 * @return array of radius hits
	 */
	public int[] getRediusFrequency();

	/****
	 * update frequency number of the requested data
	 * @param iata
	 * @param radius
	 */
	public void updateAirportFrequencyData(String iata, Double radius);
	
	
	public void clearAll();
}
