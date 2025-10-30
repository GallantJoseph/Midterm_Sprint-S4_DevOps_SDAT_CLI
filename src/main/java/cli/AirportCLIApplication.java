package cli;

import rest.RESTClient;

import java.util.List;
import java.util.Scanner;

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

            report.append("\n\n" + city.getName() + ", " + city.getProvince() + "\n********************\n\n");

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


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String serverURL = "http://localhost:8080/";

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

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n=== ALL AIRPORTS BY CITY ===");
                    System.out.println(airportCLIApplication.generateAirportsByCityReport());
                }
                case "2" -> {
                    System.out.print("Enter passenger ID: ");
                    String input = scanner.nextLine().trim();
                    try {
                        Long id = Long.parseLong(input);
                        System.out.println("\n=== AIRCRAFT FOR PASSENGER " + id + " ===");
                        //System.out.println(airportCLIApplication.generateAircraftReportForPassenger(id));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Must be a number.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter aircraft ID: ");
                    String input = scanner.nextLine().trim();
                    try {
                        Long id = Long.parseLong(input);
                        System.out.println("\n=== AIRPORTS FOR AIRCRAFT " + id + " ===");
                        //System.out.println(app.generateAirportsByAircraftReport(id));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Must be a number.");
                    }
                }
                case "4" -> {
                        System.out.println("\n=== AIRPORTS USED BY PASSENGER ===");
                        //System.out.println(airportCLIApplication.generateAirportsByPassengerReport());
                }
                case "5" -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }
}