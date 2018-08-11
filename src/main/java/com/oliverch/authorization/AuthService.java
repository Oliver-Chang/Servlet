package com.oliverch.authorization;



public class AuthService {
    public Boolean verifyJWT(String jwt) {
        return AuthManager.VerifyToken(jwt);
    }
}
