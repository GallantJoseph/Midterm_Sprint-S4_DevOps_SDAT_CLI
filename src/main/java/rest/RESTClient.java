package rest;

import java.net.http.HttpClient;

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
}
