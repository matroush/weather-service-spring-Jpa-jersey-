package com.myservice.weather.adapter;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import com.myservice.weather.domain.AirportData;

public interface AirportDataAdapter {

	public boolean add(AirportData ad);

	public AirportData findOne(Predicate<AirportData> condition);

	public Collection<AirportData> findAll(Predicate<AirportData> condition);

	public Set<String> codeSet();
}
