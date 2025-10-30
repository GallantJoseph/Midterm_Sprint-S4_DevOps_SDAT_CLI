package cli;

import domain.Airport;
import domain.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.RESTClient;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AirportCLIApplicationTest {
    
    @Mock
    private RESTClient mockRESTClient;

    @Test
    public void testGenerateAirportsByCityReport() {
        AirportCLIApplication airportCLIApplicationUnderTest = new AirportCLIApplication();

        List<City> cityList = new ArrayList<>();
        List<Airport> airportList = new ArrayList<>();

        City city = new City("St. John's", "NL", 150000);
        city.setId(1);

        cityList.add(city);

        Airport airport1 = new Airport("St. John's Airport", "SJN", city);
        Airport airport2 = new Airport("Deer Lake Airport", "DLN", city);

        airportList.add(airport1);
        airportList.add(airport2);

        Mockito.when(mockRESTClient.getAllCities()).thenReturn(cityList);
        Mockito.when(mockRESTClient.getAirportsByCityId(1L)).thenReturn(airportList);

        airportCLIApplicationUnderTest.setRestClient(mockRESTClient);

        Assertions.assertTrue(airportCLIApplicationUnderTest.generateAirportsByCityReport().contains("SJN"));
    }
}