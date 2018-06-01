package com.github.nekoneko.goflippy.gson;

public class User {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private Group[] groups;
    private Attribute[] attributes;

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Group[] getGroups() {
        return groups;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }
}