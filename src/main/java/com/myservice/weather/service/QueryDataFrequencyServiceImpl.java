package com.myservice.weather.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class QueryDataFrequencyServiceImpl implements QueryDataFrequencyService {

	private Map<String, Integer> requestIataFrequencyMap = new ConcurrentHashMap<String, Integer>();
	private Map<Double, Integer> requestRadiusFrequencyMap = new ConcurrentHashMap<Double, Integer>();

	public Map<String, Double> getRequestIataFrequency(Set<String> airportsCodes) {

		int size = airportsCodes.size();

		Map<String, Double> resultMap = new HashMap<String, Double>(size);

		airportsCodes.stream()
				.forEach(iataCode -> resultMap.put(iataCode, (double) requestIataFrequencyMap.getOrDefault(iataCode,0) / size));

		return resultMap;
	}

	public int[] getRediusFrequency() {

		int m = requestRadiusFrequencyMap.keySet().stream().max(Double::compare).orElse(0.0).intValue() + 1;
		int i = 0;
		int[] hist = new int[m];
		for (Map.Entry<Double, Integer> e : requestRadiusFrequencyMap.entrySet()) {
			i = e.getKey().intValue() % 10;
			hist[i] += e.getValue();
		}

		return hist;
	}

	public void updateAirportFrequencyData(String iata, Double radius) {

		requestIataFrequencyMap.put(iata, requestIataFrequencyMap.getOrDefault(iata, 0) + 1);
		requestRadiusFrequencyMap.put(radius, requestRadiusFrequencyMap.getOrDefault(radius, 0) + 1);

	}

	public void clearAll() {
		requestIataFrequencyMap.clear();
		requestRadiusFrequencyMap.clear();

	}

	@Override
	protected void finalize() throws Throwable {
		clearAll();
		requestIataFrequencyMap = null;
		requestRadiusFrequencyMap = null;
		super.finalize();
	}
}
