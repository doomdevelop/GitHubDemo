package org.kozlowski.githubdemo.data.model;

/**
 * Created by and on 26.05.17.
 */

public class User {
    private final String userName;
    private final String userToken;

    public User(String userName, String userToken) {
        this.userName = userName;
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserToken() {
        return userToken;
    }

}
