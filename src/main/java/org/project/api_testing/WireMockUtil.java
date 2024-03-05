package org.project.api_testing;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WireMockUtil {

    private static WireMockServer wireMockServer;

    public static void startServer(int port) {
        wireMockServer = new WireMockServer(port);
        wireMockServer.start();
        WireMock.configureFor(port);
    }

    public static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    public static void loadMappingsFromJson(String jsonFilePath) throws IOException {
        String jsonContent = Files.readString(new File(jsonFilePath).toPath());
        StubMapping stubMapping = StubMapping.buildFrom(jsonContent);
        wireMockServer.addStubMapping(stubMapping);
    }

    public static Response sendPostRequest(String path, String body, Map<String, String> headers) {
        return RestAssured.given().headers(headers).body(body).post("http://localhost:8080" + path);
    }

    public static Map<String, String> getResponseHeaders() {
        List<StubMapping> stubMappings = wireMockServer.getStubMappings();
        StubMapping lastStubMapping = stubMappings.get(stubMappings.size() - 1);
        HttpHeaders httpHeaders = lastStubMapping.getResponse().getHeaders();

        // Convert HttpHeaders to Map<String, String>
        Map<String, String> responseHeaders = new HashMap<>();
        for (String headerName : httpHeaders.keys()) {
            List<String> headerValues = httpHeaders.getHeader(headerName).values();
            // Concatenate multiple header values into a single string
            String concatenatedHeaderValues = String.join(",", headerValues);
            responseHeaders.put(headerName, concatenatedHeaderValues);
        }
        return responseHeaders;
    }
}
