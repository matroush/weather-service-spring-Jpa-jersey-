package com.myservice.weather.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myservice.weather.AppConstants;
import com.myservice.weather.adapter.AirportDataAdapterSet;
import com.myservice.weather.adapter.AtmosphericInfoAdapterSet;
import com.myservice.weather.domain.AirportData;
import com.myservice.weather.domain.AtmosphericInformation;
import com.myservice.weather.domain.DataPoint;
import com.myservice.weather.enums.DataPointType;
import com.myservice.weather.errorhandling.WeatherException;
import com.myservice.weather.predicate.AirportDataPredicate;
import com.myservice.weather.predicate.AtmosphericInfoPredicate;

@Service
public class WeatherCollectorServiceImpl implements WeatherCollectorService {

	@Autowired
	private AtmosphericInfoAdapterSet atmosphericInfoAdapterSet;

	@Autowired
	private AirportDataAdapterSet airportDataAdapterSet;

	//
	// Internal support methods
	//

	/**
	 * Update the airports weather data with the collected data.
	 *
	 * @param iataCode
	 *            the 3 letter IATA code
	 * @param pointType
	 *            the point type {@link DataPointType}
	 * @param dp
	 *            a datapoint object holding pointType data
	 *
	 * @throws WeatherException
	 *             if the update can not be completed
	 */
	public void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {

		AtmosphericInformation ai = atmosphericInfoAdapterSet.findOne(AtmosphericInfoPredicate.codeEquals(iataCode));

		if (ai == null)
			throw new WeatherException(AppConstants.AIRPORT_NOT_EXIST_CODE, AppConstants.AIRPORT_NOT_EXIST_MSG + iataCode);

		updateAtmosphericInformation(ai, pointType, dp);
	}

	/**
	 * update atmospheric information with the given data point for the given
	 * point type
	 *
	 * @param ai
	 *            the atmospheric information object to update
	 * @param pointType
	 *            the data point type as a string
	 * @param dp
	 *            the actual data point
	 */
	public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp)
			throws WeatherException {

		if (pointType.equalsIgnoreCase(DataPointType.WIND.name())) {
			if (dp.getMean() >= 0) {
				ai.setWind(dp);
				ai.setLastUpdateTime(System.currentTimeMillis());
				return;
			}
		}

		if (pointType.equalsIgnoreCase(DataPointType.TEMPERATURE.name())) {
			if (dp.getMean() >= -50 && dp.getMean() < 100) {
				ai.setTemperature(dp);
				ai.setLastUpdateTime(System.currentTimeMillis());
				return;
			}
		}

		if (pointType.equalsIgnoreCase(DataPointType.HUMIDTY.name())) {
			if (dp.getMean() >= 0 && dp.getMean() < 100) {
				ai.setHumidity(dp);
				ai.setLastUpdateTime(System.currentTimeMillis());
				return;
			}
		}

		if (pointType.equalsIgnoreCase(DataPointType.PRESSURE.name())) {
			if (dp.getMean() >= 650 && dp.getMean() < 800) {
				ai.setPressure(dp);
				ai.setLastUpdateTime(System.currentTimeMillis());
				return;
			}
		}

		if (pointType.equalsIgnoreCase(DataPointType.CLOUDCOVER.name())) {
			if (dp.getMean() >= 0 && dp.getMean() < 100) {
				ai.setCloudCover(dp);
				ai.setLastUpdateTime(System.currentTimeMillis());
				return;
			}
		}

		if (pointType.equalsIgnoreCase(DataPointType.PRECIPITATION.name())) {
			if (dp.getMean() >= 0 && dp.getMean() < 100) {
				ai.setPrecipitation(dp);
				ai.setLastUpdateTime(System.currentTimeMillis());
				return;
			}
		}

		throw new WeatherException(AppConstants.INVALID_ATMOSPHERIC_DATA_CODE, AppConstants.INVALID_ATMOSPHERIC_DATA_MSG);
	}

	/**
	 * Add a new known airport to our list.
	 *
	 * @param iataCode
	 *            3 letter code
	 * @param latitude
	 *            in degrees
	 * @param longitude
	 *            in degrees
	 * @throws WeatherException
	 *
	 * 
	 */
	@Override
	public synchronized void addAirport(String iataCode, String latString, String longString) throws WeatherException {

		
	
		
		String regex = "(^[a-zA-Z]{3}$)";

		String latRegex = "(^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,9}$)";

		String longRegex = "(^-?([1]?[1-7][1-9]|[1]?[1-8][0]|[1-9]?[0-9])\\.{1}\\d{1,9}$)";

		if (iataCode == null || latString == null || latString.equals("") || longString == null
				|| longString.equals(""))

			throw new WeatherException(AppConstants.MISSING_INPUT_CODE, AppConstants.MISSING_INPUT_MSG);

		if (!iataCode.matches(regex))
			throw new WeatherException(AppConstants.INVALID_AIRPORT_DATA_CODE,
					"Invalid iataCode Note: Valid code should be only three  letters");

		if (!latString.matches(latRegex) || !longString.matches(longRegex))
			throw new WeatherException(AppConstants.INVALID_AIRPORT_DATA_CODE, "Invalid  coordinates values ");

		if (airportDataAdapterSet.findOne(AirportDataPredicate.codeEquals(iataCode)) != null)
			throw new WeatherException(AppConstants.AIRPORT_ALREADY_EXIST_CODE,
					AppConstants.AIRPORT_ALREADY_EXIST_MSG );

		double latitude = Double.valueOf(latString);
		double longitude = Double.valueOf(longString);

		// builder factory

		airportDataAdapterSet.add(new AirportData(iataCode, latitude, longitude));
		atmosphericInfoAdapterSet.add(new AtmosphericInformation(iataCode));

	}

	@Override
	public synchronized void deleteAirportByCode(String iataCode) throws WeatherException {

		AtmosphericInformation ai = atmosphericInfoAdapterSet.findOne(AtmosphericInfoPredicate.codeEquals(iataCode));
		atmosphericInfoAdapterSet.remove(ai);

		AirportData ad = airportDataAdapterSet.findOne(AirportDataPredicate.codeEquals(iataCode));

		if (ad == null)
			throw new WeatherException(AppConstants.AIRPORT_NOT_EXIST_CODE, AppConstants.AIRPORT_NOT_EXIST_MSG);

		airportDataAdapterSet.remove(ad);

	}

	@Override
	public Set<String> getAirports() {
		return airportDataAdapterSet.codeSet();
	}

	@Override
	public AirportData getAirport(String iata) {

		return airportDataAdapterSet.findOne(AirportDataPredicate.codeEquals(iata));
	}

}
