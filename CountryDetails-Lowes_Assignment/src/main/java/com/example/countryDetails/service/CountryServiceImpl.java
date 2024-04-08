package com.example.countryDetails.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.countryDetails.model.CountryInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountryServiceImpl implements CountryService {
	
    @Value("${api.base.url}")
    private String baseUrl;
    
	
	private final RestTemplate restTemplate;
	
	@Override
	public CountryInfo getCountryInfo(String countryName) throws JsonMappingException, JsonProcessingException
	{
		CountryInfo countryInfo=new CountryInfo();
		List<String> bordersList=new ArrayList<>();
		List<String> countryOficialLanguages=new ArrayList<>();
		List<Integer> latLongValues=new ArrayList<>();
		String url=baseUrl+"/name/"+countryName+"?fullText=true&fields=name,languages,latlng,car,borders";
		HttpEntity<?> requestEntity = new HttpEntity<>(null);
		ResponseEntity<String> countryDetails=restTemplate.exchange(url, HttpMethod.GET,requestEntity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode responseBody = objectMapper.readTree(countryDetails.getBody());
		 
	       for (JsonNode node : responseBody) {
	            JsonNode name = node.get("borders");
	                for (JsonNode border : name) {
	                    bordersList.add(border.asText());
	            }
	              countryInfo.setBorders(bordersList);	
	              JsonNode carSide = node.get("car").get("side");
	              countryInfo.setDrivingSide(carSide.asText());
	                
	              JsonNode officialName = node.get("name").get("official");
	              countryInfo.setName(officialName.asText());
	              
	              JsonNode countryLanguages = node.get("languages");
	              for (JsonNode languages : countryLanguages) {
	            	  countryOficialLanguages.add(languages.asText());
	            }
	              countryInfo.setLanguages(countryOficialLanguages);
	              
	              JsonNode countryLatLong=node.get("latlng");
	              for (JsonNode latlong : countryLatLong) {
	            	  latLongValues.add(latlong.asInt());
	            }
	              countryInfo.setLatLong(latLongValues);	               
	       }  
	        	       	
		log.info("Parent Country Info {}",countryInfo);
		return countryInfo;
		
	}
	
	@Override
	public List<CountryInfo> getNeighbouringCountriesDetails(List<String> countryCodes) throws JsonMappingException, JsonProcessingException
	{
		List<CountryInfo> ls=new ArrayList<>();

		String countryCcaCodes="";
		for(String codes:countryCodes)
		{
			countryCcaCodes+=codes;
			countryCcaCodes+=",";
		}
		String url=baseUrl+"alpha?codes="+countryCcaCodes+"&fields=name,languages,latlng,car,borders";
		HttpEntity<?> requestEntity = new HttpEntity<>(null);
		ResponseEntity<String> countryDetails=restTemplate.exchange(url, HttpMethod.GET,requestEntity, String.class);
		
		ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonArray = mapper.readTree(countryDetails.getBody());
        List<JsonNode> jsonNodes = new ArrayList<>();
        if (jsonArray.isArray()) {
            for (JsonNode node : jsonArray) {
                jsonNodes.add(node);
            }
        }
        for (JsonNode node : jsonNodes) {
        CountryInfo countryInfo=new CountryInfo();
		List<String> countryOficialLanguages=new ArrayList<>();
		List<Integer> latLongValues=new ArrayList<>();
          JsonNode carSide = node.get("car").get("side");
          countryInfo.setDrivingSide(carSide.asText());
            
          JsonNode officialName = node.get("name").get("common");
          countryInfo.setName(officialName.asText());
          
          JsonNode countryLanguages = node.get("languages");
          for (JsonNode languages : countryLanguages) {
        	  countryOficialLanguages.add(languages.asText());
        }
          countryInfo.setLanguages(countryOficialLanguages);
          
          JsonNode countryLatLong=node.get("latlng");
          for (JsonNode latlong : countryLatLong) {
        	  latLongValues.add(latlong.asInt());
        }
          countryInfo.setLatLong(latLongValues);
          ls.add(countryInfo);
        }
        
		log.info("Neighbouring Country Info {}",ls);
		return ls;	
	}
}

