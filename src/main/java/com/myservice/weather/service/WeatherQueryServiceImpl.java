package com.myservice.weather.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.myservice.weather.adapter.AirportDataAdapterSet;
import com.myservice.weather.adapter.AtmosphericInfoAdapterSet;
import com.myservice.weather.domain.AirportData;
import com.myservice.weather.domain.AtmosphericInformation;
import com.myservice.weather.errorhandling.WeatherException;
import com.myservice.weather.predicate.AirportDataPredicate;
import com.myservice.weather.predicate.AtmosphericInfoPredicate;
import com.myservice.weather.util.ServiceUtil;

@Service
public class WeatherQueryServiceImpl implements WeatherQueryService {

	
	
	@Autowired
	private AirportDataAdapterSet airportDataSet;

	@Autowired
	private AtmosphericInfoAdapterSet atmosphericInfoSet;

	@Autowired
	private QueryDataFrequencyService dataFrequencyService;

	@Override
	public Map<String, Object> ping() {


		
		Map<String, Object> retval = new HashMap<>();

		//Count only data which is updated in the last day
		long datasize = atmosphericInfoSet
				.size(AtmosphericInfoPredicate.hasData().and(AtmosphericInfoPredicate.updatedInLastDay()));

		Set<String> iataCodeSet = airportDataSet.codeSet();

		Map<String, Double> freq = dataFrequencyService.getRequestIataFrequency(iataCodeSet);

		int [] radiusFreq = dataFrequencyService.getRediusFrequency();
		
		retval.put("datasize", datasize);
		retval.put("iata_freq", freq);
		retval.put("radius_freq",radiusFreq );

		return retval;
	}


	@Override
	public List<AtmosphericInformation> weather(String iata, double radius) throws WeatherException {


		List<AtmosphericInformation> retval = new ArrayList<AtmosphericInformation>();

		//Validate not exist return empty list
		if(airportDataSet.findOne(AirportDataPredicate.codeEquals(iata)) == null)
			return retval;
		
			
		dataFrequencyService.updateAirportFrequencyData(iata, radius);

		if (radius == 0) {

			AtmosphericInformation returnedObj = atmosphericInfoSet.findOne(AtmosphericInfoPredicate.codeEquals(iata));

			retval.add(returnedObj);

		} else {
			
			// Get near airports
			List<String> matchedCodes = findNearAirports(iata, radius);

			// Get near atmospheric data of near airports which has data
			retval = atmosphericInfoSet
					.findAll(AtmosphericInfoPredicate.codeWithin(matchedCodes).and(AtmosphericInfoPredicate.hasData()));

		}
		return retval;
	}

	/****
	 * Find near air ports based on the distance difference
	 * @param iataCode
	 * @param radius
	 * @return List of iata codes of near air ports
	 */
	private List<String> findNearAirports(String iataCode, double radius) {

		AirportData requestedAirport = airportDataSet.findOne(AirportDataPredicate.codeEquals(iataCode));

		List<String> codeList = new ArrayList<String>();

		airportDataSet.stream().filter((c -> ServiceUtil.calculateDistance(requestedAirport, c) <= radius))
				.forEach(c -> codeList.add(c.getIata()));

		return codeList;
	}


	@Override
	public List<AtmosphericInformation> getRainyAtmosphericInfoList() throws WeatherException {
		
		return (List<AtmosphericInformation>) atmosphericInfoSet.stream().filter(AtmosphericInfoPredicate.isRainy());
	}

	

}
