package com.oliverch.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oliverch.admin.Administrator;

import java.util.Date;

public class AuthManager {

    private static final String SECRET_KEY = "SECRET_KEY";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final String ISSUER = "OLIVER";
    public static String createToken(Administrator administrator) {
        Date EXPIRES_AT = new Date(System.currentTimeMillis() +  30 * 60 * 1000);
        String username = administrator.getUsername();
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("username", username)
                .withExpiresAt(EXPIRES_AT)
                .sign(ALGORITHM);
    }

    public static Boolean VerifyToken(String token) {
        Boolean ret = false;
        if (token == null) {
            return false;
        }
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            Claim username = jwt.getClaim("username");
            if (username != null)
                ret = true;

        } catch (JWTVerificationException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }
}
