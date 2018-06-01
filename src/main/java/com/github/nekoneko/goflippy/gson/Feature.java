package com.github.nekoneko.goflippy.gson;

public class Feature {
    private String key;
    private boolean enabled;

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
