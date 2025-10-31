package cli;

import domain.Aircraft;
import domain.Airport;
import domain.City;
import domain.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.RESTClient;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AirportCLIApplicationTest {
    
    @Mock
    private RESTClient mockRESTClient;

/*  ---------------------------------------------------------------
    Question 1.	What airports are there in each city?
    --------------------------------------------------------------- */
    @Test
    public void testQ1_generateAirportsByCityReport() {
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

        Assertions.assertTrue(airportCLIApplicationUnderTest.generateAirportsByCityReport().contains("DLN"));
        Assertions.assertFalse(airportCLIApplicationUnderTest.generateAirportsByCityReport().contains("BAD"));;
    }


/*  ---------------------------------------------------------------
    Question 2.	What aircraft has each passenger flown on?
    --------------------------------------------------------------- */
    @Test
    public void testQ2_displayMultipleAircraft() {
        AirportCLIApplication airportCLIApplication = new AirportCLIApplication();

        List<Aircraft> aircraft = new ArrayList<>();
        Aircraft a1 = new Aircraft("Boeing 737", "Air Canada", 180);
        a1.setId(1L);
        Aircraft a2 = new Aircraft("Airbus A320", "Westjet", 160);
        a2.setId(2L);
        aircraft.add(a1);
        aircraft.add(a2);

        Mockito.when(mockRESTClient.getAircraftByPassengerId(5L)).thenReturn(aircraft);
        airportCLIApplication.setRestClient(mockRESTClient);
        String report = airportCLIApplication.generateAircraftReportForPassenger(5L);
        String expected = "Boeing 737 - 180\n" +
                "Airbus A320 - 160";
        Assertions.assertEquals(expected, report);
    }
    
    @Test
    public void testQ2_noAircraft_showMessage() {
        AirportCLIApplication airportCLIApplication = new AirportCLIApplication();

        Mockito.when(mockRESTClient.getAircraftByPassengerId(99L)).thenReturn(new ArrayList<>());

        airportCLIApplication.setRestClient(mockRESTClient);

        String expected = "No aircraft found for passenger 99";
        String report = airportCLIApplication.generateAircraftReportForPassenger(99L);

        Assertions.assertEquals(expected, report);
    }


/*  ---------------------------------------------------------------
    Question 3.	What airports do aircraft take off from and land at?
    --------------------------------------------------------------- */
@Test
public void testQ3_displayNameCode() {
    AirportCLIApplication app = new AirportCLIApplication();

    List<Airport> airports = new ArrayList<>();
    
    Airport yyt = new Airport();
    yyt.setId(1L);
    yyt.setCode("YYT");
    yyt.setName("St. John's Airport");

    Airport yyz = new Airport();
    yyz.setId(2L);
    yyz.setCode("YYZ");
    yyz.setName("Pearson");

    airports.add(yyt);
    airports.add(yyz);

    Mockito.when(mockRESTClient.getAirportByAircraftId(7L)).thenReturn(airports);
    app.setRestClient(mockRESTClient);

    String report = app.generateAirportsByAircraftReport(7L);

    String expected =
        "St. John's Airport - YYT\n" +
        "Pearson - YYZ";

    Assertions.assertEquals(expected, report);
}

@Test
public void testQ3_noAirports_showMessage() {
    AirportCLIApplication app = new AirportCLIApplication();

    Mockito.when(mockRESTClient.getAirportByAircraftId(999L)).thenReturn(new ArrayList<>());
    app.setRestClient(mockRESTClient);

    String report = app.generateAirportsByAircraftReport(999L);

    String expected = "No airports found for aircraft ID 999";

    Assertions.assertEquals(expected, report);
}
    


/*  ---------------------------------------------------------------
    Question 4.	What airports have passengers used?
    --------------------------------------------------------------- */
    @Test
    public void testQ4_displaysAllPassengersAndTheirAirports() {
        AirportCLIApplication app = new AirportCLIApplication();

        Passenger passenger1 = new Passenger();
        passenger1.setId(1L);
        passenger1.setFirstName("Bob");
        passenger1.setLastName("Ross");

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFirstName("Nicolas");
        passenger2.setLastName("Cage");

        Airport yyt = new Airport();
        yyt.setId(1L);
        yyt.setCode("YYT");
        yyt.setName("St. John's");

        Airport yyz = new Airport();
        yyz.setId(2L);
        yyz.setCode("YYZ");
        yyz.setName("Pearson");

        Map<Passenger, Set<Airport>> map = new LinkedHashMap<>();
        map.put(passenger1, new LinkedHashSet<>(List.of(yyt, yyz)));
        map.put(passenger2, new LinkedHashSet<>(List.of(yyz)));

        Mockito.when(mockRESTClient.getPassengersWithTheirAirports()).thenReturn(map);
        app.setRestClient(mockRESTClient);

        String report = app.generateAirportsByPassengerReport();

        String expected =
                "=== AIRPORTS USED BY EACH PASSENGER ===\n\n" +
                        "Bob Ross\n" +
                        "  YYT - St. John's\n" +
                        "  YYZ - Pearson\n" +
                        "\n" +
                        "Nicolas Cage\n" +
                        "  YYZ - Pearson\n" +
                        "\n";

        Assertions.assertEquals(expected, report);
    }

    @Test
    public void testQ4_noPassengers_showsMessage() {
        AirportCLIApplication app = new AirportCLIApplication();

        Mockito.when(mockRESTClient.getPassengersWithTheirAirports()).thenReturn(new HashMap<>());

        app.setRestClient(mockRESTClient);

        String report = app.generateAirportsByPassengerReport();

        Assertions.assertTrue(report.contains("No passengers found."));
    }

    @Test
    public void testQ4_passengerWithNoAirports_showsParenthesesMessage() {
        AirportCLIApplication app = new AirportCLIApplication();

        Passenger passenger3 = new Passenger();
        passenger3.setId(3L);
        passenger3.setFirstName("Justin");
        passenger3.setLastName("Greenslade");

        Map<Passenger, Set<Airport>> map = new HashMap<>();
        map.put(passenger3, new HashSet<>());

        Mockito.when(mockRESTClient.getPassengersWithTheirAirports()).thenReturn(map);

        app.setRestClient(mockRESTClient);

        String report = app.generateAirportsByPassengerReport();

        Assertions.assertTrue(report.contains("Justin Greenslade"));
        Assertions.assertTrue(report.contains("  (No airports)"));
    }

    
/*  ---------------------------------------------------------------
    ***** Additional Tests *****
    --------------------------------------------------------------- */

    @Test
    public void testGetAirportsByAircraftById() {
        AirportCLIApplication airportCLIApplicationUnderTest = new AirportCLIApplication();

        City city = new City("St. John's", "NL", 150000);
        city.setId(1L);

        Aircraft aircraft = new Aircraft("ABC", "YTT", 40);
        aircraft.setId(1L);

        List<Airport> airports = new ArrayList<>();

        Airport airport = new Airport("St. John's Airport", "SJN", city);
        airport.setId(1L);

        airports.add(airport);

        Mockito.when(mockRESTClient.getAircraftById(1L)).thenReturn(aircraft);
        Mockito.when(mockRESTClient.getAircraftById(2L)).thenReturn(null);

        Mockito.when(mockRESTClient.getAirportByAircraftId(1L)).thenReturn(airports);

        airportCLIApplicationUnderTest.setRestClient(mockRESTClient);

        Assertions.assertEquals(mockRESTClient.getAircraftById(1L), aircraft); 
        Assertions.assertEquals(mockRESTClient.getAircraftById(2L), null); 
        Assertions.assertEquals(mockRESTClient.getAirportByAircraftId(1L), airports); 
    }
    
}