package com.example.voting_back.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @Test
    void generate_and_verify_token_success() {
        JwtUtil jwtUtil = new JwtUtil("test-secret");

        String token = jwtUtil.generateToken(123L, "alice");

        DecodedJWT jwt = jwtUtil.verify(token);
        assertEquals(123L, jwtUtil.getUserId(jwt));
        assertEquals("alice", jwtUtil.getUsername(jwt));
    }

    @Test
    void verify_throws_for_invalid_token() {
        JwtUtil jwtUtil = new JwtUtil("test-secret");
        assertThrows(JWTVerificationException.class, () -> jwtUtil.verify("not-a-jwt-token"));
    }
}
