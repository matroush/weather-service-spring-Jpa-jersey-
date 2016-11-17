package com.myservice.weather.util;

import com.myservice.weather.AppConstants;
import com.myservice.weather.domain.AirportData;

public final class ServiceUtil {

	/***
	 * Calculate the distance difference between two airports
	 * @param ad1
	 * @param ad2
	 * @return difference
	 */
	public static double calculateDistance(AirportData ad1, AirportData ad2) {
		double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
		double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
		double a = Math.pow(Math.sin(deltaLat / 2), 2)
				+ Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
		double c = 2 * Math.asin(Math.sqrt(a));
		return AppConstants.R * c;
	}
}
