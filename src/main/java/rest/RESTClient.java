package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Airport;
import domain.City;
import domain.Aircraft;
import domain.Passenger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class RESTClient {
    private String serverURL;
    private HttpClient client;

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public HttpClient getClient() {
        if (client == null) {
            client = HttpClient.newHttpClient();
        }

        return client;
    }

    private HttpResponse<String> httpSender(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            System.out.println("Error Status Code: " + response.statusCode());
        }
        return response;
    }

    public List<City> getAllCities() {
        String allCitiesURL = serverURL + "cities";

        List<City> cities = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(allCitiesURL)).build();

        try {
            HttpResponse<String> response = httpSender(request);

            cities = buildCityListFromResponse(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public List<City> buildCityListFromResponse(String response) throws JsonProcessingException {
        List<City> cities = new ArrayList<City>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        cities = mapper.readValue(response, new TypeReference<List<City>>() {
        });

        return cities;
    }

    // Number 1
    public List<Airport> getAirportsByCityId(Long cityId) {
        String airportByCityURL = serverURL + "airports/city/" + cityId;

        List<Airport> airports = new ArrayList<Airport>();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(airportByCityURL)).build();

        try {
            HttpResponse<String> response = httpSender(request);

            airports = buildAirportListFromResponse(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return airports;
    }

    public List<Airport> buildAirportListFromResponse(String response) throws JsonProcessingException {
        List<Airport> airports = new ArrayList<Airport>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        airports = mapper.readValue(response, new TypeReference<List<Airport>>() {
        });

        return airports;
    }

    //Number 2
    public List<Aircraft> getAircraftByPassengerId(Long passengerId) {
        List<Aircraft> aircraftList = new ArrayList<Aircraft>();
        String airportByCityURL = serverURL + "aircraft/passenger/" + passengerId;
      
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(airportByCityURL)).build();

        try {
            HttpResponse<String> response = httpSender(request);

            aircraftList = buildAircraftListFromResponse(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return aircraftList;
    }

    public List<Aircraft> buildAircraftListFromResponse(String response) throws JsonProcessingException {
        List<Aircraft> aircraftList = new ArrayList<Aircraft>();
      
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        aircraftList = mapper.readValue(response, new TypeReference<List<Aircraft>>() {
        });

        return aircraftList;
    }
    //Number 3
    // number 4
    public Map<Passenger, Set<Airport>> getPassengersWithTheirAirports() {
        Map<Passenger, Set<Airport>> result = new LinkedHashMap<>();

        try {

            String url = serverURL + "passengers";
            HttpResponse<String> response = httpSender(HttpRequest.newBuilder().uri(URI.create(url)).build());
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<Passenger> passengers = mapper.readValue(response.body(), new TypeReference<List<Passenger>>() {});


            for (Passenger p : passengers) {
                Set<Airport> airports = new HashSet<>();
                List<Aircraft> aircraftList = getAircraftByPassengerId(p.getId());

                for (Aircraft a : aircraftList) {
                    if (a.getAirports() != null) {
                        airports.addAll(a.getAirports());
                    }
                }
                result.put(p, airports);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return result;
    }
}
