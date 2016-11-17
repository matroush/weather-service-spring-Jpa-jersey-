package com.myservice.weather;

import static java.lang.String.format;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.myservice.weather.endpoint.RestWeatherCollectorEndpoint;
import com.myservice.weather.endpoint.RestWeatherQueryEndpoint;
import com.myservice.weather.errorhandling.GenericExceptionMapper;
import com.myservice.weather.errorhandling.WeatherExceptionMapper;


/**
 * This main method will be use by the automated functional grader. You shouldn't move this class or remove the
 * main method. You may change the implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {


	
	WeatherServer(){
	}
	
 
    public static void main(String[] args) {
        try {
        	
        	String baseUrl = null ;
        	            
        	Properties prop = new Properties();
			String propFileName = "config.properties";
 			InputStream inputStream = WeatherServer.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 	
			baseUrl = 	prop.getProperty("server.url");
           
			System.out.println("Starting Weather App local testing server: " + baseUrl);
            
			
            final ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.register(RestWeatherCollectorEndpoint.class);
            resourceConfig.register(RestWeatherQueryEndpoint.class);
            resourceConfig.register(WeatherExceptionMapper.class);
            resourceConfig.register(GenericExceptionMapper.class);

            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUrl), resourceConfig, false);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdownNow();
            }));

            HttpServerProbe probe = new HttpServerProbe.Adapter() {
                public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection, Request request) {
                    System.out.println(request.getRequestURI()+" "+request.getParameterMap());
                    
                }
            };
            server.getServerConfiguration().getMonitoringConfig().getWebServerConfig().addProbes(probe);


            // the autograder waits for this output before running automated tests, please don't remove it
            server.start();
            System.out.println(format("Weather Server started.\n url=%s\n", baseUrl));

            // blocks until the process is terminated
            Thread.currentThread().join();
            server.shutdown();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
