package com.github.nekoneko.goflippy.client;

import com.github.nekoneko.goflippy.cache.CacheStore;
import com.github.nekoneko.goflippy.config.GoFlippyConfigBuilder;
import com.github.nekoneko.goflippy.gson.*;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class GoFlippyClientTest {
    private static final int MOCK_SERVER_PORT = 18080;
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

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
    public void serverErrorThenReturnDefaultValueWhenCallFeatureEnabled() throws Exception {
        setupMockServer(500, this.gson.toJson(new User()));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("user-uuid");

        assertTrue(client.featureEnabled("Feature-Key", user, true));
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
        UserFeature expected = new UserFeature();
        expected.setEnabled(true);

        setupMockServer(200, this.gson.toJson(expected));

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        User user = new User();
        user.setUuid("uuid-A");
        assertEquals(expected.isEnabled(), client.featureEnabled("feature-A", user, false));
    }

    @Test
    public void getFeatureEnabledWithUserWriteCache() throws Exception {
        String featureKey1 = "feature-A";
        String uuid1 = "uuid-A";
        Feature expectedFeature = new Feature();
        expectedFeature.setKey(featureKey1);
        UserFeature expected = new UserFeature();
        expected.setFeature(expectedFeature);
        expected.setEnabled(true);

        setupMockServer(200, this.gson.toJson(expected));
        CacheStore cs = mock(CacheStore.class);

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build(), cs);
        User user = new User();
        user.setUuid(uuid1);
        assertEquals(expected.isEnabled(), client.featureEnabled(featureKey1, user, false));

        ArgumentCaptor<UserFeature> ac = ArgumentCaptor.forClass(UserFeature.class);
        verify(cs).put(eq(featureKey1 + uuid1), ac.capture());
        assertEquals(ac.getValue().isEnabled(), expected.isEnabled());
        assertThat(ac.getValue().getFeature(), is(samePropertyValuesAs(expected.getFeature())));
    }

    @Test
    public void getFeatureEnabledWithUserReadCache() throws Exception {
        String featureKey1 = "feature-A";
        String uuid1 = "uuid-A";
        Feature expectedFeature = new Feature();
        expectedFeature.setKey(featureKey1);
        UserFeature expected = new UserFeature();
        expected.setFeature(expectedFeature);
        expected.setEnabled(true);

        setupMockServer(500, "Test NG");
        CacheStore cs = mock(CacheStore.class);
        doReturn(expected).when(cs).get(featureKey1.concat(uuid1));
        doReturn(true).when(cs).cacheEnabled();

        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build(), cs);
        User user = new User();
        user.setUuid(uuid1);
        assertEquals(expected.isEnabled(), client.featureEnabled(featureKey1, user, false));
    }

    @Test
    public void getFeature() throws Exception {
        UUID expectedUuid1 = new UUID();
        expectedUuid1.setUuid("uuid-1");
        UUID expectedUuid2 = new UUID();
        expectedUuid2.setUuid("uuid-2");
        UUID[] uuids = new UUID[]{expectedUuid1, expectedUuid2};
        ToggleFilter expectedFilter1 = new ToggleFilter();
        expectedFilter1.setType(ToggleFilter.Type.UUID);
        expectedFilter1.setUuids(uuids);
        ToggleFilter[] expectedFilters = new ToggleFilter[]{expectedFilter1};
        Feature expected = new Feature();
        expected.setKey("feature-A");
        expected.setEnabled(true);
        expected.setFilters(expectedFilters);

        setupMockServer(200, this.gson.toJson(expected));
        GoFlippyClient client = new GoFlippyClient(new GoFlippyConfigBuilder().uri(String.format("http://localhost:%d", MOCK_SERVER_PORT)).apiKey("TEST-API-KEY").build());
        Feature actual = client.getFeature("feature-A");
        assertEquals(expected.isEnabled(), actual.isEnabled());
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getFilters().length, actual.getFilters().length);
        for (int i = 0; i < expected.getFilters().length; i++) {
            assertThat(expected.getFilters()[0].getUuids()[i], is(samePropertyValuesAs(actual.getFilters()[0].getUuids()[i])));
        }
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
