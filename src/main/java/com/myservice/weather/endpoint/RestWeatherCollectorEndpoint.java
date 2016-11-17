package com.myservice.weather.endpoint;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.myservice.weather.AppConstants;
import com.myservice.weather.domain.AirportData;
import com.myservice.weather.domain.DataPoint;
import com.myservice.weather.errorhandling.ErrorMessage;
import com.myservice.weather.errorhandling.WeatherException;
import com.myservice.weather.service.WeatherCollectorService;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
@Component
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
	public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

	/** shared gson json to object factory */
	public final static Gson gson = new Gson();

	/**
	 * To follow MVC pattern all business delegated to service layer class
	 * WeatherCollectorService
	 **/
	@Autowired
	private WeatherCollectorService weatherCollectorService;

	@Override
	public Response ping()  {
		LOGGER.info("GET collect/ping ");
		return Response.status(Response.Status.OK).entity("ready").build();
	}

	@Override
	public Response updateWeather(String iataCode, String pointType, String datapointJson) throws WeatherException {
		LOGGER.info("POST collect/weather/ Parms " + iataCode + "," + pointType + "," + datapointJson);
		weatherCollectorService.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));

		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response getAirports()  {

		LOGGER.info("GET collect/airports ");
		return Response.status(Response.Status.OK).entity(weatherCollectorService.getAirports()).build();
	}

	@Override
	public Response getAirport(@PathParam("iata") String iata) {

		LOGGER.info("GET collect/airport/" + iata);

		AirportData ad = weatherCollectorService.getAirport(iata);
		return Response.status(Response.Status.OK).entity(ad).build();
	}

	@Override
	public Response addAirport(String iata, String latString, String longString) throws WeatherException {
		
		LOGGER.info("POST collect/airport/ Parms " + iata + "," + latString + "," + longString);

		weatherCollectorService.addAirport(iata, latString, longString);

		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response deleteAirport(String iata) throws WeatherException {

		LOGGER.log(Level.INFO, "DELETE collect/airport/ Parms " + iata);

		weatherCollectorService.deleteAirportByCode(iata);

		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response exit() {
		System.exit(0);
		return Response.noContent().build();
	}

}
