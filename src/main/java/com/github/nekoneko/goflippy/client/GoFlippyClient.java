package com.github.nekoneko.goflippy.client;

import com.github.nekoneko.goflippy.cache.CacheStore;
import com.github.nekoneko.goflippy.cache.InMemoryCacheStore;
import com.github.nekoneko.goflippy.config.GoFlippyConfig;
import com.github.nekoneko.goflippy.gson.Feature;
import com.github.nekoneko.goflippy.gson.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GoFlippyClient {
    private final GoFlippyConfig config;
    private final CacheStore store;
    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();
    private static final String HTTP_HEADER_API_KEY = "X-API-Key";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Constructor
     *
     * @param config GoFlippy configuration instance
     * @param store  cache store instance
     */
    public GoFlippyClient(GoFlippyConfig config, CacheStore store) {
        this.config = config;
        this.store = store;
        this.httpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.SECONDS))
                .connectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * Constructor
     *
     * @param config GoFlippy configuration instance
     */
    public GoFlippyClient(GoFlippyConfig config) {
        this(config, new InMemoryCacheStore(config.getCacheLifeTimeSeconds()));
    }

    /**
     * Register a user
     *
     * @param user User data
     * @return registered user object
     */
    public boolean registerUser(User user) {
        RequestBody body = RequestBody.create(JSON, this.gson.toJson(user));
        Request request = new Request.Builder()
                .url(String.format("%s/v1/users", this.config.getUri()))
                .addHeader(HTTP_HEADER_API_KEY, this.config.apiKey())
                .post(body)
                .build();

        try {
            Response response = this.httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }

            this.gson.fromJson(response.body().string(), User.class);
            return true;
        } catch(JsonSyntaxException je) {
            return false;
        } catch(IOException e) {
            return false;
        }
    }

    /**
     * Get feature enabled?
     *
     * @param key          feature key
     * @param user         user instance
     * @param defaultValue default value
     * @return true is feature enabled
     */
    public boolean featureEnabled(String key, User user, boolean defaultValue) {
        Feature feature = fromStore(key, user.getUuid());
        if (feature != null) {
            return feature.isEnabled();
        }

        String endpoint = String.format("%s/v1/users/%s/features/%s", this.config.getUri(), user.getUuid(), key);
        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader(HTTP_HEADER_API_KEY, this.config.apiKey())
                .build();
        try {
            Response response = this.httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }

            feature = this.gson.fromJson(response.body().string(), Feature.class);
            this.store.put(user.getUuid(), feature);

            return feature.isEnabled();
        } catch(JsonSyntaxException je) {
            return defaultValue;
        } catch (IOException e) {
            return defaultValue;
        }
    }

    /**
     * Get feature from cache store
     *
     * @param key cache key
     * @return Feature instance or null(cache not found or expired)
     */
    private Feature fromStore(String key) {
        if (this.store.cacheEnabled()) {
            return this.store.get(key);
        }

        return null;
    }

    /**
     * Get feature from cache store
     *
     * @param key  cache key
     * @param uuid user unique id
     * @return Feature instance or null(cache not found or expired)
     */
    private Feature fromStore(String key, String uuid) {
        return this.fromStore(key.concat(uuid));
    }
}
