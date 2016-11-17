package com.myservice.weather.errorhandling;

public class WeatherException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ErrorMessage errorMessage;
	
	public WeatherException(){
		
	}
	
	public WeatherException(String msg){
		
		super(msg);
	}
	
	
	public WeatherException(String code , String msg){
		
		super(msg);
		errorMessage = new ErrorMessage(code, msg);
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
}
