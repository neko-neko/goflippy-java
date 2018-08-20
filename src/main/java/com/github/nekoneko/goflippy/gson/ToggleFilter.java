package com.github.nekoneko.goflippy.gson;

import com.google.gson.annotations.SerializedName;

public class ToggleFilter {
    private Type type;
    private Group[] groups;
    private Attribute[] attributes;
    private UUID[] uuids;
    private ReleaseDateTime releaseDateTime;
    private Percentage percentage;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public UUID[] getUuids() {
        return uuids;
    }

    public void setUuids(UUID[] uuids) {
        this.uuids = uuids;
    }

    public ReleaseDateTime getReleaseDateTime() {
        return releaseDateTime;
    }

    public void setReleaseDateTime(ReleaseDateTime releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    public Percentage getPercentage() {
        return percentage;
    }

    public void setPercentage(Percentage percentage) {
        this.percentage = percentage;
    }

    public enum Type {
        @SerializedName("group")
        GROUP,
        @SerializedName("attribute")
        ATTRIBUTE,
        @SerializedName("uuid")
        UUID,
        @SerializedName("release_date_time")
        RELEASE_DATE_TIME,
        @SerializedName("percentage")
        PERCENTAGE
    }
}
