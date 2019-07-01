package com.operation.database.entity;

/**
 * @Author: Qinyadong
 * @Date: 2018/12/28 10:27
 * @Since:
 */
public class DataSource {
    private String url;
    private String username;
    private String password;
    private String schame;

    public DataSource(String url, String username, String password, String schame) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.schame = schame;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getSchame() {
        return schame;
    }

    public void setSchame(String schame) {
        this.schame = schame;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", schame='" + schame + '\'' +
                '}';
    }
}
