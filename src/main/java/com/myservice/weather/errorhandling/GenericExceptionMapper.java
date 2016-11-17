package com.myservice.weather.errorhandling;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.myservice.weather.AppConstants;

@Provider
@Singleton
/***
 * 
 * @author matroush
 *
 */
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
 
	public Response toResponse(Throwable ex) {
		
		ErrorMessage errorMessage = new ErrorMessage(AppConstants.GENERAL_ERROR_CODE ,AppConstants.GENERAL_ERROR_MSG+" "+ex.getMessage() );		
		

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();	
	}


}
