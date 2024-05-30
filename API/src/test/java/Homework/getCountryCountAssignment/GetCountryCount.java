package Homework.getCountryCountAssignment;

import utils.FunctionLibrary;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;

public class GetCountryCount {
	String countryCountUrl = "https://jsonmock.hackerrank.com/api/countries/search";

	@Test
	public void testMethod() {
		System.out.println(getCountries1("z", 800000));
		
	}
	public int getCountries1(String s, int p) {

String response = submitGetRequest(1, "s");
		int totalPages = JsonPath.read(response, "$.total_pages");
		System.out.println("Total pages: " + totalPages);
int countryCount=0;
		List<String> countries = new ArrayList<>();
		for (int i = 1; i <= totalPages; i++) {
			response = submitGetRequest(i, "s");
			countries.addAll(JsonPath.read(response, "$.data[?(@.population>" + p + ")].name"));
			System.out.println("Loop: " + i + " List size: " + countries.size());
		}
		for (String eachCountry : countries) {
			if (eachCountry.contains(s)) { countryCount++;
				System.out.println(eachCountry);
				
			}}
			System.out.println("There are "+countryCount+" countries with applied parameters");
		
		return countryCount;

//		List<String> names = JsonPath.read(response, "$.data[*].name");
//		List<String> data = JsonPath.read(response, "$.data[*]");
//		List<String> countriesBasedOnPopulation = JsonPath.read(response, "$.data[?(@.population>" + p + ")].name");
//		for(String country: countriesBasedOnPopulation) {
//		if (country.contains(s)) {
//			countryCount++;
//
//		}}
//		return countryCount;

	}
public String submitGetRequest(int page, String name) {
		
		Response response = given()
							.param("page", page)
							.param("name", name)
							.when()
							.get(countryCountUrl);
		
		return response.asString();
	}
	
}
