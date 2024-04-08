package com.example.countryDetails.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class CountryInfo {
	String name;
	List<Integer> latLong;
	List<String> borders;
	String drivingSide;
	List<String> languages;	
}

