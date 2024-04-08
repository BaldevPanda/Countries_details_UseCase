# Countries_details_UseCase
The controller is having one GET endpoint to retrieve the details of the neighbouring countries and their similarities with parent country with parameters like Sharing any of the official Languages, Same car driving side and the approximate distance between the countries in miles.

GET Endpoint: http://localhost:8082/country/neighbour/similarities/{Country_Name}

Bordering countries of {Country Name} are [List of neighbouring countries]
Output format:
Country name
official language -- Yes/No
Car diving side -- Yes/No
Approx Distance –- x miles
