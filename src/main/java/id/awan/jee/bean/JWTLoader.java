package id.awan.jee.bean;

import id.awan.jee.util.LZ4CompressionAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwe;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.CompressionAlgorithm;
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

@ApplicationScoped
public class JWTLoader {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final CompressionAlgorithm compressionAlgorithm;
    private final String issuer;
    private final String keyId;
    private final long expiredDays;
    private final JacksonSerializer<Object> jacksonSerializer;

    public JWTLoader() {
        this.publicKey = null;
        this.privateKey = null;
        this.compressionAlgorithm = null;
        this.issuer = null;
        this.keyId = null;
        this.expiredDays = 0;
        this.jacksonSerializer = null;
    }

    @Inject
    public JWTLoader(
            ResourceLoader resourceLoader,
            RSAKeyLoader rsaKeyLoader
    ) {
        byte[] privateKeyBytes = resourceLoader.loadResourceAsBytes("key/privateKey.pkcs8.der");
        byte[] publicKeyBytes = resourceLoader.loadResourceAsBytes("key/publicKey.der");

        this.privateKey = rsaKeyLoader.privateKey(privateKeyBytes);
        this.publicKey = rsaKeyLoader.publicKey(publicKeyBytes);

        this.jacksonSerializer = new JacksonSerializer<>();

//        this.compressionAlgorithm = Jwts.ZIP.GZIP;
//        this.compressionAlgorithm = Jwts.ZIP.DEF;
        this.compressionAlgorithm = new LZ4CompressionAlgorithm();

        this.issuer = "JEE";
        this.keyId = "ssos";
        this.expiredDays = 999;
    }

    public String generateJWS(
            String sub,
            String aud,
            String appId
    ) {
        Instant nowInstant = Instant.now();
        return Jwts.builder()
                .header()
                .keyId(keyId).and()
                .audience().add(Collections.of(aud)).and()
                .subject(sub)
                .claim("appId", appId)
                .issuer(issuer)
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(nowInstant.plus(expiredDays, ChronoUnit.DAYS)))
                .compressWith(compressionAlgorithm)
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
    }

    public Jws<Claims> parseJWS(String jws) {
        return Jwts.parser()
                .zip().add(compressionAlgorithm)
                .and()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(jws);
    }

    public String generateJWE(
            String sub,
            String aud,
            String appId
    ) {
        Instant nowInstant = Instant.now();
        return Jwts.builder()
                .header()
                .keyId(keyId).and()
                .audience().add(Collections.of(aud)).and()
                .subject(sub)
                .claim("appId", appId)
                .issuer(issuer)
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(nowInstant.plus(expiredDays, ChronoUnit.DAYS)))
                .compressWith(compressionAlgorithm)
                .encryptWith(publicKey, Jwts.KEY.RSA_OAEP, Jwts.ENC.A256CBC_HS512)
                .compact();
    }

    public Jwe<Claims> parseJWE(String jwe) {
        return Jwts.parser()
                .zip().add(compressionAlgorithm)
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
        byte[] rsaPrivateKeySerialize = jacksonSerializer.serialize(rsaPrivateJwk);
        return new String(rsaPrivateKeySerialize);
    }

    public RsaPrivateJwk parseRsaKeyJwk(String rsaPrivateKeyJwk) {
        return (RsaPrivateJwk) Jwks.parser()
                .build()
                .parse(rsaPrivateKeyJwk);
    }

}
