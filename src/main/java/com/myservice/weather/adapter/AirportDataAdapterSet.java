package com.myservice.weather.adapter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.myservice.weather.domain.AirportData;

/***
 * Works as adapter class for the entire airport data set 
 * @author matroush
 *
 */
@Component
@Scope("singleton")
public final class AirportDataAdapterSet extends HashSet<AirportData> implements AirportDataAdapter {

	/**
	 * Return a set of only iata code of all airports
	 */
	@Override
	public Set<String> codeSet() {
		
		Set<String> iataCodeSet = new HashSet<String>(size());
		
		stream().forEach(a->iataCodeSet.add(a.getIata()));
		
		return iataCodeSet;
	}

	/**
     * find the first matched airport based on the condition parameter , else return null;
     *
     * @param condition to filter the result by
     * @return matched object or null
     */
	@Override
	public AirportData findOne(Predicate<AirportData> condition) {
		return super.stream().filter(condition).findFirst().orElse(null);
	}
	
	/**
     * find all matched airports based on the condition parameter
     *
     * @param condition to filter the result by
     * @return matched set
     */
	@Override
	public Set<AirportData> findAll(Predicate<AirportData> condition) {
		return super.stream().filter(condition).collect(Collectors.toSet());
	}

}
