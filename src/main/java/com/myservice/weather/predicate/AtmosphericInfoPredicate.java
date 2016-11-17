package com.myservice.weather.predicate;

import java.util.List;
import java.util.function.Predicate;

import com.myservice.weather.domain.AtmosphericInformation;

public class AtmosphericInfoPredicate {

	/****
	 * Create a jdk1.8 predicate for entire AtmosphericInformation where at
	 * least one of its data points has value
	 * 
	 * @return predict object
	 */
	public static Predicate<AtmosphericInformation> hasData() {

		return a -> a.getCloudCover() != null || a.getHumidity() != null || a.getPressure() != null
				|| a.getPrecipitation() != null || a.getTemperature() != null || a.getWind() != null;

	}

	/****
	 * Create a jdk1.8 predicate for entire AtmosphericInformation where at
	 * least one of its data points updated in last day
	 * 
	 * @return predict object
	 */
	public static Predicate<AtmosphericInformation> updatedInLastDay() {

		return a -> a.getLastUpdateTime() > System.currentTimeMillis() - 86400000;

	}

	/****
	 * Create a jdk1.8 predicate where the entire iterator object should have
	 * the same iataCode
	 * 
	 * @param iata
	 * @return predict object
	 */
	public static Predicate<AtmosphericInformation> codeEquals(String iata) {

		return a -> a.getIata().equals(iata);

	}

	public static Predicate<AtmosphericInformation> codeWithin(List<String> iataCodeList) {

		return a -> iataCodeList.contains(a.getIata());

	}

	public static Predicate<AtmosphericInformation> isRainy() {
		return a -> a.getCloudCover().getMean() > 0 || a.getPrecipitation().getMean() > 0 ;
	}
}
