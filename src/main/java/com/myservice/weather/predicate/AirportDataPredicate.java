package com.myservice.weather.predicate;

import java.util.function.Predicate;

import com.myservice.weather.domain.AirportData;

public class AirportDataPredicate {


	/****
	 * Create a jdk1.8 predicate where the entire iterator object should have the same iataCode
	 * @param iata
	 * @return predict object
	 */
	public static Predicate<AirportData> codeEquals(String iata) {

		return a -> a.getIata().equals(iata);

	}
}
