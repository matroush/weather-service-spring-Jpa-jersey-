package com.myservice.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

	public final static Logger LOGGER = Logger.getLogger(AirportLoader.class.getName());

	/** end point for read queries */
	private WebTarget query;

	/** end point to supply updates */
	private WebTarget collect;

	public AirportLoader(String weatherServiceUrl) {
		
		
		Client client = ClientBuilder.newClient();
		query = client.target(weatherServiceUrl+"/query");
		collect = client.target(weatherServiceUrl+"/collect");
	}

	public void upload( File airportDataFile) throws IOException {

		LOGGER.info("Parsing file " + airportDataFile.getCanonicalPath());
		String line = null;
		String cvsSplitBy = ",";
		final String addAirportPath = "/airport";
		int lineCount = 0;
		int failedCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(airportDataFile))) {

			while ((line = br.readLine()) != null) {

				lineCount++;

				line = line.replaceAll("\"", "");
				// use comma as separator
				String[] airportData = line.split(cvsSplitBy);

				WebTarget path = collect.path(
						addAirportPath + "/" + airportData[2] + "/" + airportData[4] + "/" + airportData[5] + "/");

				Response response = path.request().post(Entity.json(null));

				if (response.getStatus() != Response.Status.OK.getStatusCode()) {

					failedCount++;
					LOGGER.warning("Line " + line + " is not inserted due to the this cause " + response.getEntity());
				}

			}

			// PRINT SUMMARY

			LOGGER.info("Total procssed line count : " + lineCount + " , Failed lines : " + failedCount);

		} catch (IOException e) {

			LOGGER.severe(
					"Error while parsing file " + airportDataFile.getCanonicalPath() + " Cause " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	  
	public static void main(String args[]) throws IOException {
		
		String fileName = args[0] ;
		
		ClassLoader classLoader = WeatherServer.class.getClassLoader();
		
		URL inputDataFile = classLoader.getResource(fileName);
		
		    

		
		File airportDataFile = new File(inputDataFile.getFile());


		if (!airportDataFile.exists() || airportDataFile.length() == 0) {
			System.err.println(airportDataFile + " is not a valid input");
			System.exit(1);
		}

		
       	String weatherServiceUrl = null ;
        
    	Properties prop = new Properties();
		String propFileName = "config.properties";
			InputStream inputStream = WeatherServer.class.getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
	
		weatherServiceUrl = prop.getProperty("server.url");
		
		
		AirportLoader al = new AirportLoader(weatherServiceUrl);
		al.upload( airportDataFile);
		System.exit(0);
	}
}
