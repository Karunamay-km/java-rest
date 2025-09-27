package org.karunamay.core.authentication.JWT;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jwt.class);

    @Setter
    private Map<String, ?> headers = new HashMap<>();
    @Setter
    private Map<String, ?> claims = new HashMap<>();
    @Getter
//    private final LocalDateTime accessTokenExpiration = new Date(System.currentTimeMillis() + 3600);
    private final LocalDateTime accessTokenExpiration = LocalDateTime.now().plusSeconds(3600);
    @Getter
    private final LocalDateTime refreshTokenExpiration = LocalDateTime.now().plusSeconds(3600_000);
    private final Date issuedAt = new Date();
    private final String issuer = "core-api";
    private final String audience = "client";

    @Getter
    @Setter
    private String subject;



    public static void generateKeys() {
        File pvtKey = new File("keys/private_key.pem");
        File pubKey = new File("keys/public_key.pem");

        if (pvtKey.exists() || pubKey.exists()) {
            LOGGER.info("Keys are already generated");
            return;
        }

        KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
        try {
            pvtKey.getParentFile().mkdirs();
            pubKey.getParentFile().mkdirs();
            try (
                    FileOutputStream pvtos = new FileOutputStream(pvtKey);
                    FileOutputStream pubos = new FileOutputStream(pubKey)
            ) {
                pvtos.write(keyPair.getPrivate().getEncoded());
                pubos.write(keyPair.getPublic().getEncoded());
            }
            LOGGER.info("Secret keys has been generated inside 'keys' folder.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String createJwt() {
        try {
            return Jwts.builder()
                    .issuer(issuer)
                    .audience().add(audience).and()
                    .expiration(Date.from(accessTokenExpiration.atZone(ZoneId.systemDefault()).toInstant()))
                    .issuedAt(issuedAt)
                    .subject(subject)
                    .claims(claims)
                    .header().add(headers).and()
                    .signWith(KeyLoader.loadPrivateKey())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createRefreshToken() {
        try {
            return Jwts.builder()
                    .issuer(issuer)
                    .audience().add(audience).and()
                    .expiration(Date.from(accessTokenExpiration.atZone(ZoneId.systemDefault()).toInstant()))
                    .issuedAt(issuedAt)
                    .claims(claims)
                    .header().add(headers).and()
                    .signWith(KeyLoader.loadPrivateKey())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(KeyLoader.loadPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (Exception e) {
            LOGGER.error("Error occurred when validating token: {} ", e.getMessage(), e);
            return false;
        }
    }

    public Jws<Claims> parseClaims(String jwt) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(KeyLoader.loadPublicKey())
                    .build()
                    .parseSignedClaims(jwt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Header getHeader() {
        return Jwts.header().build();
    }

    public static class KeyLoader {

        public static PrivateKey loadPrivateKey() throws Exception {
            byte[] bytes = Files.readAllBytes(Paths.get("keys/private_key.pem"));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        }

        public static PublicKey loadPublicKey() throws Exception {
            byte[] bytes = Files.readAllBytes(Paths.get("keys/public_key.pem"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        }

    }
}
