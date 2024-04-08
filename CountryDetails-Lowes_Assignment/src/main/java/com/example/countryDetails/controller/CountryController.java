package com.example.countryDetails.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.example.countryDetails.model.CountryInfo;
import com.example.countryDetails.model.CountrySimilarityDTO;
import com.example.countryDetails.service.CountryService;
import com.example.countryDetails.util.SimilarityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CountryController {
	
	private final CountryService countryService;
	
	private final SimilarityUtils similaritiesUtil;
	
	@GetMapping("/neighbour/similarities/{countryName}")
	public ResponseEntity<List<CountrySimilarityDTO>> getNeighbourCountries( @PathVariable ("countryName") String country) throws JsonMappingException, JsonProcessingException
	{
		try {
			CountryInfo parentCountry = countryService.getCountryInfo(country);
			List<CountryInfo> neighbouringCountries = countryService.getNeighbouringCountriesDetails(parentCountry.getBorders());
			List<CountrySimilarityDTO>answers = neighbouringCountries.stream().map(s->similaritiesUtil.getSimilaritiesAndDistance(s, parentCountry)).collect(Collectors.toList());
			List<String> countries=neighbouringCountries.stream().map(s->s.getName()).collect(Collectors.toList());
			log.info("Bordering countries of "+country+" are "+ countries);
			for(CountrySimilarityDTO borderCountriesSimilarities:answers)
			{
				
				log.info(borderCountriesSimilarities.getName());
				log.info("official language -- {}", borderCountriesSimilarities.getOfficialLanguage());
				log.info("Car diving side -- {}", borderCountriesSimilarities.getCarDrivingSide());
				log.info("Approx Distance –- {} miles", borderCountriesSimilarities.getApproxDistance());
			}
			return new ResponseEntity<>(answers, HttpStatus.OK);
		} catch(HttpClientErrorException e){
			log.error("Error while calling the endpoints {}", e.getStatusCode());
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch(Exception e){
			log.error("Exception while calling the endpoints");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

