package com.github.nekoneko.goflippy.gson;

public class UserFeature {
    private boolean enabled;
    private Feature feature;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

}
