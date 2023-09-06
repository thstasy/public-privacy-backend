package org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword;

public class PasswordUpdateRequest {

    private String token;
    private String password;

    public PasswordUpdateRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }
    public PasswordUpdateRequest() {
        // Default constructor
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
