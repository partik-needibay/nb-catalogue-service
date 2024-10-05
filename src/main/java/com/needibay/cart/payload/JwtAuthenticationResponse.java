package com.needibay.cart.payload;

public class JwtAuthenticationResponse {
	
	private String accessToken;
    private String tokenType = "Bearer";
    private String role = "admin";
    private Object userData;

    public JwtAuthenticationResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserData(Object userInfo){
        this.userData = userInfo;
    }

    public Object getUserData(){
        return userData;
    }

}
