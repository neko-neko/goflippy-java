package com.github.nekoneko.goflippy.client;

import com.github.nekoneko.goflippy.cache.CacheStore;
import com.github.nekoneko.goflippy.config.GoFlippyConfigBuilder;
import com.github.nekoneko.goflippy.gson.Feature;
import com.github.nekoneko.goflippy.gson.User;
import com.google.gson.Gson;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GoFlippyClientTest {
    private static final int MOCK_SERVER_PORT = 18080;
    private final Gson gson = new Gson();
    private MockWebServer mockServer;

    @Test
    public void receiveInvalidStringThenReturnFalseWhenCallRegisterUser() throws Exception {
        setupMockServer(200, "invalid string");

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();

        assertFalse(client.registerUser(user));
    }

    @Test
    public void lostConnectionThenReturnFalseWhenCallRegisterUser() throws Exception {
        setupMockServer(200, this.gson.toJson(new User()));

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
        setupMockServer(500, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();

        assertFalse(client.registerUser(user));
    }

    @Test
    public void receiveInvalidStringThenReturnFalseWhenCallFeatureEnabled() throws Exception {
        setupMockServer(200, "invalid string");

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("user-uuid");

        assertFalse(client.featureEnabled("Feature-Key", user, false));
    }

    @Test
    public void lostConnectionThenReturnFalseWhenCallFeatureEnabled() throws Exception {
        setupMockServer(200, this.gson.toJson(new User()));

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
        setupMockServer(500, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("user-uuid");

        assertFalse(client.featureEnabled("Feature-Key", user, false));
    }

    @Test
    public void sentValidRequestWithApiKeyToRegisterUser() throws Exception {
        setupMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        client.registerUser(user);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("TEST-API-KEY", request.getHeaders().get("X-API-Key"));
    }

    @Test
    public void sentValidRequestWithPathToRegisterUser() throws Exception {
        setupMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        client.registerUser(user);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("/v1/users", request.getPath());
    }

    @Test
    public void sentValidRequestWithMethodToRegisterUser() throws Exception {
        setupMockServer(200, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        client.registerUser(user);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void sentValidRequestWithBodyToRegisterUser() throws Exception {
        setupMockServer(200, this.gson.toJson(new User()));
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
        setupMockServer(200, this.gson.toJson(new Feature()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        client.featureEnabled("feature-A", user, false);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("TEST-API-KEY", request.getHeaders().get("X-API-Key"));
    }

    @Test
    public void sentValidRequestWithPathToFeature() throws Exception {
        setupMockServer(200, this.gson.toJson(new Feature()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        client.featureEnabled("feature-A", user, false);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals(String.format("/v1/users/%s/features/feature-A", user.getUuid()), request.getPath());
    }

    @Test
    public void sentValidRequestWithMethodToFeature() throws Exception {
        setupMockServer(200, this.gson.toJson(new Feature()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        client.featureEnabled("feature-A", user, false);
        RecordedRequest request = this.mockServer.takeRequest();
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void getFeatureEnabledWithUser() throws Exception {
        Feature expectedFeature = new Feature();
        expectedFeature.setKey("feature-A");
        expectedFeature.setEnabled(true);

        setupMockServer(200, this.gson.toJson(expectedFeature));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        assertEquals(expectedFeature.isEnabled(), client.featureEnabled("feature-A", user, false));
    }

    @Test
    public void getFeatureEnabledWithUserWriteCache() throws Exception {
        String featureKey1 = "feature-A";
        String uuid1 = "uuid-A";
        Feature expectedFeature = new Feature();
        expectedFeature.setKey(featureKey1);
        expectedFeature.setEnabled(true);

        setupMockServer(200, this.gson.toJson(expectedFeature));
        CacheStore cs = mock(CacheStore.class);

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build(), cs);
        User user = new User();
        user.setUuid(uuid1);
        assertEquals(expectedFeature.isEnabled(), client.featureEnabled(featureKey1, user, false));

        ArgumentCaptor<Feature> ac = ArgumentCaptor.forClass(Feature.class);
        verify(cs).put(eq(featureKey1 + uuid1), ac.capture());
        assertEquals(ac.getValue().getKey(), expectedFeature.getKey());
        assertEquals(ac.getValue().isEnabled(), expectedFeature.isEnabled());
    }

    @Test
    public void getFeatureEnabledWithUserReadCache() throws Exception {
        String featureKey1 = "feature-A";
        String uuid1 = "uuid-A";
        Feature expectedFeature = new Feature();
        expectedFeature.setKey(featureKey1);
        expectedFeature.setEnabled(true);

        setupMockServer(500, "Test NG");
        CacheStore cs = mock(CacheStore.class);
        doReturn(expectedFeature).when(cs).get(featureKey1.concat(uuid1));
        doReturn(true).when(cs).cacheEnabled();

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build(), cs);
        User user = new User();
        user.setUuid(uuid1);
        assertEquals(expectedFeature.isEnabled(), client.featureEnabled(featureKey1, user, false));

    }


    @After
    public void shutdownMockServer() throws Exception {
        if (this.mockServer != null) {
            this.mockServer.shutdown();
        }
    }

    private void setupMockServer(int code, String body) throws Exception {
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
