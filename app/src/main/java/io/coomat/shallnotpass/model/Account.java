package io.coomat.shallnotpass.model;

import java.util.UUID;

public class Account {

    private String uuid;
    private String site;
    private String username;
    private String password;

    public Account(String site, String username, String password) {
        this.uuid = UUID.randomUUID().toString();
        this.site = site;
        this.username = username;
        this.password = password;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
