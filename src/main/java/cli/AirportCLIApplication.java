package cli;

import rest.RESTClient;

public class AirportCLIApplication
{
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

    public static void main(String[] args) {
        String serverURL = "http://localhost:8080/";

        AirportCLIApplication airportCLIApplication = new AirportCLIApplication();

        if (args.length == 1)
            serverURL = args[0];

        RESTClient restClient = new RESTClient();
        restClient.setServerURL(serverURL);

        airportCLIApplication.setRestClient(restClient);

        System.out.println(serverURL);
    }
}