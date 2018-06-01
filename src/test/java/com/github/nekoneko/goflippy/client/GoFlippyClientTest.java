package com.github.nekoneko.goflippy.client;

import com.github.nekoneko.goflippy.config.GoFlippyConfigBuilder;
import com.github.nekoneko.goflippy.gson.Feature;
import com.github.nekoneko.goflippy.gson.User;
import com.google.gson.Gson;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GoFlippyClientTest {
    private static final int MOCK_SERVER_PORT = 18080;
    private final Gson gson = new Gson();
    private MockWebServer mockServer;

    @Test
    public void receiveInvalidStringThenReturnFalseWhenCallRegisterUser() throws Exception {
        setupUserMockServer(200, "invalid string");

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();

        assertFalse(client.registerUser(user));
    }

    @Test
    public void lostConnectionThenReturnFalseWhenCallRegisterUser() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder()
                .uri(String.format("http://localhost:%d", MOCK_SERVER_PORT + 1))
                .apiKey("TEST-API-KEY")
                .writeTimeout(1)
                .build()
        );
        User user = new User();

        assertFalse(client.registerUser(user));
    }

    @Test
    public void serverErrorThenReturnFalseWhenCallRegisterUser() throws Exception {
        setupUserMockServer(500, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();

        assertFalse(client.registerUser(user));
    }

    @Test
    public void receiveInvalidStringThenReturnFalseWhenCallFeatureEnabled() throws Exception {
        setupUserMockServer(200, "invalid string");

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("user-uuid");

        assertFalse(client.featureEnabled("Feature-Key", user, false));
    }

    @Test
    public void lostConnectionThenReturnFalseWhenCallFeatureEnabled() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder()
                .uri(String.format("http://localhost:%d", MOCK_SERVER_PORT + 1))
                .apiKey("TEST-API-KEY")
                .writeTimeout(1)
                .build()
        );
        User user = new User();
        user.setUuid("user-uuid");

        assertFalse(client.featureEnabled("Feature-Key", user, false));
    }

    @Test
    public void serverErrorThenReturnFalseWhenCallFeatureEnabled() throws Exception {
        setupUserMockServer(500, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("user-uuid");

        assertFalse(client.featureEnabled("Feature-Key", user, false));
    }

    @Test
    public void sentValidRequestWithApiKeyToRegisterUser() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        client.registerUser(user);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("TEST-API-KEY", request.getHeaders().get("X-API-Key"));
    }

    @Test
    public void sentValidRequestWithPathToRegisterUser() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        client.registerUser(user);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("/v1/users", request.getPath());
    }

    @Test
    public void sentValidRequestWithMethodToRegisterUser() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        client.registerUser(user);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void sentValidRequestWithBodyToRegisterUser() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new User()));
        User user = new User();
        user.setEmail("test@example.com");
        user.setUuid("user-uuid");
        user.setLastName("last name");
        user.setFirstName("first name");

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        client.registerUser(user);

        RecordedRequest request = this.mockServer.takeRequest();
        User actualRequest = this.gson.fromJson(request.getBody().readUtf8(), User.class);
        assertEquals("test@example.com", actualRequest.getEmail());
        assertEquals("user-uuid", actualRequest.getUuid());
        assertEquals("last name", actualRequest.getLastName());
        assertEquals("first name", actualRequest.getFirstName());
    }

    @Test
    public void sentValidRequestWithApiKeyToFeature() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new Feature()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        client.featureEnabled("feature-A", user, false);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("TEST-API-KEY", request.getHeaders().get("X-API-Key"));
    }

    @Test
    public void sentValidRequestWithPathToFeature() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new Feature()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        client.featureEnabled("feature-A", user, false);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals(String.format("/v1/users/%s/features/feature-A", user.getUuid()), request.getPath());
    }

    @Test
    public void sentValidRequestWithMethodToFeature() throws Exception {
        setupUserMockServer(200, this.gson.toJson(new Feature()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        client.featureEnabled("feature-A", user, false);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void getFeatureEnabled() throws Exception {
        Feature expectedFeature = new Feature();
        expectedFeature.setKey("feature-A");
        expectedFeature.setEnabled(true);

        setupUserMockServer(200, this.gson.toJson(expectedFeature));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        assertEquals(expectedFeature.isEnabled(), client.featureEnabled("feature-A", user, false));
    }

    // TODO: Add verify get feature enabled from cache store

    @After
    public void shutdownUserMockServer() throws Exception {
        if (this.mockServer != null) {
            this.mockServer.shutdown();
        }
    }

    private void setupUserMockServer(int code, String body) throws Exception {
        final MockWebServer server = new MockWebServer();
        server.enqueue(
                new MockResponse()
                        .setResponseCode(code)
                        .setBody(body)
        );
        server.start(MOCK_SERVER_PORT);

        this.mockServer = server;
    }

}
