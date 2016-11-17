package com.myservice.weather.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.myservice.weather.domain.AtmosphericInformation;

/***
 * Works as adapter class for the entire Atmospheric information data set 
 * @author matroush
 *
 */
@Component
@Scope("singleton")
public class AtmosphericInfoAdapterSet extends HashSet<AtmosphericInformation> implements AtmosphericInfoAdapter {

	/**
     * find the first matched AtmosphericInformation based on the condition  , else return empty object
     *
     * @param condition to filter the result by
     * @return matched object
     */
	@Override
	public AtmosphericInformation findOne(Predicate<AtmosphericInformation> condition) {

		return stream().filter(condition).findFirst().orElse(new AtmosphericInformation());
	}

	/**
     * find all matched AtmosphericInformation based on the condition  
     *
     * @param condition to filter the result by
     * @return matched object
     */
	@Override
	public List<AtmosphericInformation> findAll(Predicate<AtmosphericInformation> condition) {

		return stream().filter(condition).collect(Collectors.toList());
	}

	/**
     * return the count of matched items based on the condition parameter
     *
     * @param condition to filter the result by
     * @return matched object
     */
	@Override
	public long size(Predicate<AtmosphericInformation> condition) {

		return stream().filter(condition).count();
	}

}
