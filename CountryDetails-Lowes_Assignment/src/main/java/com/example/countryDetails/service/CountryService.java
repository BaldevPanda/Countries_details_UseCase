package com.example.countryDetails.service;

import java.util.List;

import com.example.countryDetails.model.CountryInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface CountryService {
	
	CountryInfo getCountryInfo(String countryName) throws JsonMappingException, JsonProcessingException;
	List<CountryInfo> getNeighbouringCountriesDetails(List<String> countryCodes) throws JsonMappingException, JsonProcessingException;
}

