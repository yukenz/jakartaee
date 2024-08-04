package id.awan.jee.bean;

import id.awan.jee.util.LZ4CompressionAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwe;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.RsaPrivateJwk;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class JWTLoader {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final LZ4CompressionAlgorithm lz4;

    @Inject
    public JWTLoader(
            ResourceLoader resourceLoader,
            RSAKeyLoader rsaKeyLoader
    ) {
        byte[] privateKeyBytes = resourceLoader.loadResourceAsBytes("key/privateKey.pkcs8.der");
        byte[] publicKeyBytes = resourceLoader.loadResourceAsBytes("key/publicKey.der");
        this.privateKey = rsaKeyLoader.privateKey(privateKeyBytes);
        this.publicKey = rsaKeyLoader.publicKey(publicKeyBytes);
        lz4 = new LZ4CompressionAlgorithm();

    }

    public String generateJWS(
            String aud,
            String sub,
            String appId
    ) {
        Instant nowInstant = Instant.now();
        return Jwts.builder()
                .header()
                .keyId("ssos").and()
                .audience().add(Collections.of(aud)).and()
                .subject(sub)
                .claims(Map.of("appId", appId))
                .issuer("JEE")
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(nowInstant.plus(999, ChronoUnit.DAYS)))
                .compressWith(lz4)
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
    }

    public Jws<Claims> parseJWS(String jws) {
        return Jwts.parser()
                .zip().add(lz4)
                .and()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(jws);
    }

    public String generateJWE(
            String aud,
            String sub,
            String appId
    ) {
        Instant nowInstant = Instant.now();
        return Jwts.builder()
                .header()
                .keyId("ssos").and()
                .audience().add(Collections.of(aud)).and()
                .subject(sub)
                .claims(Map.of("appId", appId))
                .issuer("JEE")
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(nowInstant.plus(999, ChronoUnit.DAYS)))
                .compressWith(lz4)
                .encryptWith(publicKey, Jwts.KEY.RSA_OAEP, Jwts.ENC.A256CBC_HS512)
                .compact();
    }

    public Jwe<Claims> parseJWE(String jwe) {
        return Jwts.parser()
                .zip().add(lz4)
                .and()
                .decryptWith(privateKey)
                .build()
                .parseEncryptedClaims(jwe);
    }

    public String generateRsaPrivateKeyJwk(
            RSAPrivateKey privateKey
    ) {
        RsaPrivateJwk rsaPrivateJwk = Jwks.builder()
                .key(privateKey)
                .build();
        byte[] rsaPrivateKeySerialize = new JacksonSerializer<>().serialize(rsaPrivateJwk);
        return new String(rsaPrivateKeySerialize);
    }

    public RsaPrivateJwk parseRsaKeyJwk(String rsaPrivateKeyJwk) {
        return (RsaPrivateJwk) Jwks.parser()
                .build()
                .parse(rsaPrivateKeyJwk);
    }

}
