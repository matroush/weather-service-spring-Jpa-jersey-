package com.crossover.trial.weather;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.myservice.weather.adapter.AirportDataAdapterSet;
import com.myservice.weather.adapter.AtmosphericInfoAdapterSet;
import com.myservice.weather.domain.AirportData;
import com.myservice.weather.domain.AtmosphericInformation;
import com.myservice.weather.domain.DataPoint;
import com.myservice.weather.endpoint.WeatherCollectorEndpoint;
import com.myservice.weather.endpoint.WeatherQueryEndpoint;
import com.myservice.weather.errorhandling.WeatherException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@WebAppConfiguration
public class WeatherEndpointTest {

	@Autowired
	private AirportDataAdapterSet airportDataSet;

	@Autowired
	private AtmosphericInfoAdapterSet atmosphericInfoDataSet;

	@Autowired
	private WeatherQueryEndpoint _query;

	@Autowired
	private WeatherCollectorEndpoint _update;

	private Gson _gson = new Gson();

	private DataPoint _dp;

	@Before
	public void setUp() throws Exception {

		airportDataSet.clear();
		atmosphericInfoDataSet.clear();
		_update.addAirport("BOS", "42.364347", "-71.005181");
		_update.addAirport("EWR", "40.6925", "-74.168667");
		_update.addAirport("JFK", "40.639751", "-73.778925");
		_update.addAirport("LGA", "40.777245", "-73.872608");
		_update.addAirport("MMU", "40.79935", "-74.4148747");

		_dp = new DataPoint.Builder().withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
		_update.updateWeather("BOS", "wind", _gson.toJson(_dp));
		_query.weather("BOS", "0").getEntity();
	}

	@Test
	public void testPing() throws Exception {
		String ping = _query.ping();
		JsonElement pingResult = new JsonParser().parse(ping);
		assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
		assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
	}

	@Test
	public void testGet() throws Exception {
		List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
		assertEquals(ais.get(0).getWind(), _dp);
	}

	@Test
	public void testGetNearby() throws Exception {
		// check datasize response
		_update.updateWeather("JFK", "wind", _gson.toJson(_dp));
		_dp.setMean(40);
		_update.updateWeather("EWR", "wind", _gson.toJson(_dp));
		_dp.setMean(30);
		_update.updateWeather("LGA", "wind", _gson.toJson(_dp));

		List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("JFK", "200").getEntity();
		assertEquals(3, ais.size());
	}

	@Test
	public void testUpdate() throws Exception {

		DataPoint windDp = new DataPoint.Builder().withCount(10).withFirst(10).withMedian(50).withLast(30).withMean(22)
				.build();
		_update.updateWeather("BOS", "wind", _gson.toJson(windDp));
		_query.weather("BOS", "0").getEntity();

		String ping = _query.ping();
		JsonElement pingResult = new JsonParser().parse(ping);
		assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

		DataPoint cloudCoverDp = new DataPoint.Builder().withCount(4).withFirst(10).withMedian(60).withLast(100)
				.withMean(50).build();
		_update.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

		List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
		assertEquals(ais.get(0).getWind(), windDp);
		assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
	}

	@Test
	public void testAddAirport() throws Exception {

		_update.addAirport("UJH", "40.79935", "-74.4148747");

		Set airportList = (Set) _update.getAirports().getEntity();

		assertEquals(airportList.size(), 6);

	}

	@Test(expected=WeatherException.class)
	public void testAddAirportWithInvalidData() throws Exception {

		Response res = _update.addAirport("UJHWW", "40.79935", "-74.4148747");
		assertEquals(res.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

		res = _update.addAirport("UJH", "0", "-74.4148747");
		assertEquals(res.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

		res = _update.addAirport("OLK", "40.79935", "-1");
		assertEquals(res.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

	}

	@Test
	public void testDeleteAirport() throws Exception {

	
		Response response = _update.deleteAirport("BOS");
		assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

	
	}

	@Test(expected=WeatherException.class)
	public void testDeleteAirportAlreadyDeleted() throws Exception {
		
	
		Response response = _update.deleteAirport("BOS");
		
		// delete Already deleted
		response = _update.deleteAirport("BOS");
		assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
		
	}

	@Test
	public void testGetAirports() throws Exception {

		Response res = _update.getAirports();
		Set airportSet = (Set) res.getEntity();

		assertEquals(5, airportSet.size());
	}

	@Test
	public void testGetAirportByCode() throws Exception {

		Response res = _update.getAirport("BOS");
		AirportData retunedObj = (AirportData) res.getEntity();

		AirportData expected = new AirportData("BOS", 42.364347, -71.005181);

		assertEquals(expected, retunedObj);
	}

}