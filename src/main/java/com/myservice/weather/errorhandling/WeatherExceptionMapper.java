package com.myservice.weather.errorhandling;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton 
@Provider
public class WeatherExceptionMapper implements ExceptionMapper<WeatherException> {

	public Response toResponse(WeatherException ex) {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(ex.getErrorMessage())
				.type(MediaType.APPLICATION_JSON).
				build();
	}

}