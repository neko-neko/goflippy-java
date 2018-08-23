package com.github.nekoneko.goflippy.gson;

public class Feature {
    private String key;
    private boolean enabled;
    private ToggleFilter[] filters;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ToggleFilter[] getFilters() {
        return filters;
    }

    public void setFilters(ToggleFilter[] filters) {
        this.filters = filters;
    }

}
