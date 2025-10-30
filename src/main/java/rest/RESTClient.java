package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Aircraft;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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
    //Number 1
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
    //Number 4
}
