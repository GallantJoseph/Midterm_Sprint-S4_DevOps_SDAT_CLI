package cli;

import rest.RESTClient;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import domain.*;

public class AirportCLIApplication {
    
    private RESTClient restClient;

    public RESTClient getRestClient() {
        if (restClient == null) {
            restClient = new RESTClient();
        }

        return restClient;
    }

    public void setRestClient(RESTClient restClient) {
        this.restClient = restClient;
    }

    // Questions answered here

    // Number 1
    public String generateAirportsByCityReport() {
        List<City> cityList = getRestClient().getAllCities();

        StringBuilder report = new StringBuilder();

        for (City city : cityList) {
            List<Airport> airportList = restClient.getAirportsByCityId(city.getId());

            report.append("\n" + city.getName() + ", " + city.getProvince() + "\n********************\n");

            if (airportList.isEmpty()) {
                report.append("*** No Airports ***\n");
            } else {
                for (Airport airport : airportList) {
                    report.append(airport.getCode() + " - " + airport.getName() + "\n");
                }
            }
        }

        return report.toString();
    }

    // Number 2
    public String generateAircraftReportForPassenger(Long passengerId) {
        List<Aircraft> aircraftList = getRestClient().getAircraftByPassengerId(passengerId);
        StringBuilder report = new StringBuilder();

        if (aircraftList.isEmpty()) {
            report.append("No aircraft found for passenger ").append(passengerId);
        } else {
            for (Aircraft aircraft : aircraftList) {
                report.append(aircraft.getType())
                        .append(" - ")
                        .append(aircraft.getNumberOfPassengers())
                        .append("\n");
            }

            if (report.length() > 0) {
                report.setLength(report.length() - 1);
            }
        }
        return report.toString();
    }

    // Number 3
    public String generateAirportsByAircraftReport(Long aircraftId) {
        List<Airport> airportList = getRestClient().getAirportsByAircraftId(aircraftId);
        StringBuilder report = new StringBuilder();

        if (airportList.isEmpty()) {
            report.append("No airports found for aircraft ID ").append(aircraftId);
        } else {
            for (Airport airport : airportList) {
                report.append(airport.getName())
                    .append(" - ")
                    .append(airport.getCode())
                    .append("\n");
            }
            if (report.length() > 0) {
                report.setLength(report.length() - 1);
            }
        }

        return report.toString();
}
    
    // Number 4
    public String generateAirportsByPassengerReport() {
        Map<Passenger, Set<Airport>> data = getRestClient().getPassengersWithTheirAirports();
        StringBuilder report = new StringBuilder();

        if (data.isEmpty()) {
            report.append("No passengers found.\n");
        } else {
            for (Map.Entry<Passenger, Set<Airport>> entry : data.entrySet()) {
                Passenger p = entry.getKey();
                Set<Airport> airports = entry.getValue();

                report.append(p.getFirstName())
                        .append(" ")
                        .append(p.getLastName())
                        .append("\n");

                if (airports.isEmpty()) {
                    report.append("  (No airports)\n");
                } else {
                    for (Airport a : airports) {
                        report.append("  ")
                                .append(a.getCode())
                                .append(" - ")
                                .append(a.getName())
                                .append("\n");
                    }
                }
                report.append("\n");
            }
        }
        return report.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String serverURL = "http://localhost:8080/";

        final int AIRPORTS_BY_CITY_OPTION = 1;
        final int AIRCRAFT_BY_PASSENGER_OPTION = 2;
        final int AIRPORTS_BY_AIRCRAFT_OPTION = 3;
        final int AIRPORTS_BY_PASSENGERS_OPTION = 4;
        final int QUIT_OPTION = 5;

        int choice;

        AirportCLIApplication airportCLIApplication = new AirportCLIApplication();

        if (args.length == 1)
            serverURL = args[0];

        RESTClient restClient = new RESTClient();
        restClient.setServerURL(serverURL);

        airportCLIApplication.setRestClient(restClient);

        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("     AIRPORT MANAGEMENT APPLICATION");
            System.out.println("                MAIN MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. List all airports by city");
            System.out.println("2. Aircraft for a passenger");
            System.out.println("3. Airports for an aircraft");
            System.out.println("4. Airports used by a passenger");
            System.out.println("5. Exit");
            System.out.println("=".repeat(50));
            System.out.print("Choose (1-5): ");

            while (true) {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.print("Invalid input. Please enter a valid number: ");
                    scanner.next();
                }
            }

            switch (choice) {
                case AIRPORTS_BY_CITY_OPTION -> {
                    System.out.println("\n=== ALL AIRPORTS BY CITY ===");
                    System.out.println(airportCLIApplication.generateAirportsByCityReport());
                    pressEnterToContinue(scanner);
                }
                case AIRCRAFT_BY_PASSENGER_OPTION -> {
                    System.out.print("\nEnter passenger ID: ");
                    String input = scanner.nextLine().trim();
                    try {
                        Long id = Long.parseLong(input);
                        System.out.println("\n=== AIRCRAFT FOR PASSENGER " + id + " ===");
                        System.out.println(airportCLIApplication.generateAircraftReportForPassenger(id));

                        pressEnterToContinue(scanner);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Must be a number.");
                    }
                }
                case AIRPORTS_BY_AIRCRAFT_OPTION -> {
                    System.out.print("\nEnter aircraft ID: ");
                    String input = scanner.nextLine().trim();
                    try {
                        Long id = Long.parseLong(input);
                        System.out.println("\n=== AIRPORTS FOR AIRCRAFT " + id + " ===");
                        System.out.println(airportCLIApplication.generateAirportsByAircraftReport(id));
                        pressEnterToContinue(scanner);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Must be a number.");
                    }
                }
                case AIRPORTS_BY_PASSENGERS_OPTION -> {
                    System.out.println("\n=== AIRPORTS USED BY EACH PASSENGER ===\n");
                    System.out.println(airportCLIApplication.generateAirportsByPassengerReport());
                    pressEnterToContinue(scanner);
                }
                case QUIT_OPTION -> {
                    System.out.println("\nGoodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }

    private static void pressEnterToContinue(Scanner scanner) {
        System.out.println("\nPress Enter to Continue.");
        scanner.nextLine();
    }
}