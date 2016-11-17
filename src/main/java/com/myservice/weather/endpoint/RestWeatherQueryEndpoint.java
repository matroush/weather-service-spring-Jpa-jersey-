package com.myservice.weather.endpoint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.myservice.weather.domain.AtmosphericInformation;
import com.myservice.weather.errorhandling.WeatherException;
import com.myservice.weather.service.WeatherQueryService;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
@Component
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

	/**
	 * To follow MVC pattern all business delegated to service layer class
	 * WeatherCollectorService
	 **/
	@Autowired
	private WeatherQueryService weatherQueryService;

	public final static Logger LOGGER = Logger.getLogger("WeatherQuery");

	/** shared gson json to object factory */
	public static final Gson gson = new Gson();

	/**
	 * Retrieve service health including total size of valid data points and
	 * request frequency information.
	 *
	 * @return health stats for the service as a string
	 */
	@Override
	public String ping() {

		return gson.toJson(weatherQueryService.ping());
	}

	/**
	 * Given a query in json format {'iata': CODE, 'radius': km} extracts the
	 * requested airport information and return a list of matching atmosphere
	 * information.
	 *
	 * @param iata
	 *            the iataCode
	 * @param radiusString
	 *            the radius in km
	 *
	 * @return a list of atmospheric information
	 */
	@Override
	public Response weather(String iata, String radiusString) throws WeatherException {
		double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);

		List<AtmosphericInformation> retval = weatherQueryService.weather(iata, radius);

		return Response.status(Response.Status.OK).entity(retval).build();
	}

	@Override
	public Response getRainyAtmosphericInfo() throws WeatherException {
		
		List<AtmosphericInformation> resultList = weatherQueryService.getRainyAtmosphericInfoList();
		return Response.status(Response.Status.OK).entity(resultList).build();
	}

}
