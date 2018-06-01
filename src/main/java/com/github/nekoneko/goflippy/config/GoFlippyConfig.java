package com.github.nekoneko.goflippy.config;

public class GoFlippyConfig {
    private String uri;
    private String apiKey;
    private int cacheLifeTimeSeconds;
    private int connectionTimeout;
    private int readTimeout;
    private int writeTimeout;

    protected GoFlippyConfig(GoFlippyConfigBuilder builder) {
        this.uri = builder.uri;
        this.apiKey = builder.apiKey;
        this.cacheLifeTimeSeconds = builder.cacheLifeTimeSeconds;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
    }

    public String getUri() {
        return uri;
    }

    public String apiKey() {
        return apiKey;
    }

    public int getCacheLifeTimeSeconds() {
        return cacheLifeTimeSeconds;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }
}
