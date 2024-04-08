package com.example.countryDetails.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.countryDetails.model.CountryInfo;
import com.example.countryDetails.model.CountrySimilarityDTO;

@Component
public class SimilarityUtils {
		
	public CountrySimilarityDTO getSimilaritiesAndDistance(CountryInfo borderCountry,CountryInfo parentCountry)
	{
		CountrySimilarityDTO countrySimilarity=new CountrySimilarityDTO();
		countrySimilarity.setOfficialLanguage("No");
		countrySimilarity.setName(borderCountry.getName());		
		String drivingSide=parentCountry.getDrivingSide().equalsIgnoreCase(borderCountry.getDrivingSide())?"Yes":"No";
		countrySimilarity.setCarDrivingSide(drivingSide);
			
		for(String lang:parentCountry.getLanguages())
		{
			if(borderCountry.getLanguages().contains(lang))
				{
					countrySimilarity.setOfficialLanguage("Yes");
					break;
				}
		}		
		Long approxDistance=getApproxDistance(borderCountry.getLatLong(),parentCountry.getLatLong());
		countrySimilarity.setApproxDistance(approxDistance);
		return countrySimilarity;
			
	}
		
	//Calculate the distance between countries coordinates
	private Long getApproxDistance(List<Integer> borderCountryLatLng, List<Integer> parentCountryLatLng)
	{
		double lat2=parentCountryLatLng.get(0), lng2=parentCountryLatLng.get(1);
		double lat1=borderCountryLatLng.get(0), lng1=borderCountryLatLng.get(1);
			
		double theta = lng2 - lng1;
	    double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
	                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	                      Math.cos(Math.toRadians(theta));
	    dist = Math.acos(dist);
	    dist = Math.toDegrees(dist);
	    dist = dist * 60 * 1.1515; // Convert to miles
	    return Math.round(dist);
	}
}

