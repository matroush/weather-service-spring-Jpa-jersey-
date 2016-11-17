package com.myservice.weather.adapter;

import java.util.List;
import java.util.function.Predicate;

import com.myservice.weather.domain.AtmosphericInformation;

public interface AtmosphericInfoAdapter {

	public AtmosphericInformation findOne(Predicate<AtmosphericInformation> condition);

	public long size(Predicate<AtmosphericInformation> condition);

	public List<AtmosphericInformation> findAll(Predicate<AtmosphericInformation> condition);
}
