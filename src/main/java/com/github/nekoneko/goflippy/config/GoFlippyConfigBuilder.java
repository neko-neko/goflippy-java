package com.github.nekoneko.goflippy.config;

public class GoFlippyConfigBuilder {
    public static final int DEFAULT_CACHE_LIFE_TIME_SECONDS = 60;
    public static final int DEFAULT_CONNECTION_TIME_OUT = 10;
    public static final int DEFAULT_READ_TIME_OUT = 10;
    public static final int DEFAULT_WRITE_TIME_OUT = 10;

    protected String uri;
    protected String apiKey;
    protected int cacheLifeTimeSeconds = DEFAULT_CACHE_LIFE_TIME_SECONDS;
    protected int connectionTimeout = DEFAULT_CONNECTION_TIME_OUT;
    protected int readTimeout = DEFAULT_READ_TIME_OUT;
    protected int writeTimeout = DEFAULT_WRITE_TIME_OUT;

    public GoFlippyConfigBuilder uri(String uri) {
        this.uri = uri;
        return this;
    }

    public GoFlippyConfigBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public GoFlippyConfigBuilder cacheLifeTimeSeconds(int cacheLifeTimeSeconds) {
        this.cacheLifeTimeSeconds = cacheLifeTimeSeconds;
        return this;
    }

    public GoFlippyConfigBuilder connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public GoFlippyConfigBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public GoFlippyConfigBuilder writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public GoFlippyConfig build() {
        return new GoFlippyConfig(this);
    }
}
