package id.awan.jee;

import id.awan.jee.bean.JWTLoader;
import id.awan.jee.bean.RSAKeyLoader;
import id.awan.jee.bean.ResourceLoader;
import id.awan.jee.util.LZ4CompressionAlgorithm;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class JwtTest {

    @Test
    void jwtTest() {

        ResourceLoader resourceLoader = new ResourceLoader();
        RSAKeyLoader rsaKeyLoader = new RSAKeyLoader();

        byte[] privateKeyBytes = resourceLoader.loadResourceAsBytes("key/privateKey.pkcs8.der");
        byte[] publicKeyBytes = resourceLoader.loadResourceAsBytes("key/publicKey.der");

        PrivateKey privateKey = rsaKeyLoader.privateKey(privateKeyBytes);
        PublicKey publicKey = rsaKeyLoader.publicKey(publicKeyBytes);

        Assertions.assertNotNull(privateKey);
        Assertions.assertNotNull(publicKey);

//        Instant nowInstant = Instant.now();
//
//        LZ4CompressionAlgorithm lz4 = new LZ4CompressionAlgorithm();
//        String compact = Jwts.builder()
//                .header()
//                .keyId("ssos").and()
//                .audience().add(Collections.of("AUD")).and()
//                .subject("subject")
//                .claims(Map.of("appId", "0000"))
//                .issuer("APIGW")
//                .issuedAt(Date.from(nowInstant))
//                .expiration(Date.from(nowInstant.plus(999, ChronoUnit.DAYS)))
//                .compressWith(lz4)
////                .signWith(privateKey, Jwts.SIG.RS512)
//                .encryptWith(publicKey, Jwts.KEY.RSA_OAEP, Jwts.ENC.A256CBC_HS512)
//                .compact();

        JWTLoader jwtLoader = new JWTLoader(resourceLoader, rsaKeyLoader);
        String jwe = jwtLoader
                .generateJWE("SUB", "AUD", "0000");
        Jwe<Claims> claimsJwe = jwtLoader.parseJWE(jwe);

        String jws = jwtLoader.generateJWS("SUB", "AUD", "0000");
        Jws<Claims> claimsJws = jwtLoader.parseJWS(jws);


//        Jws<Claims> claimsJws = Jwts.parser()
//                .verifyWith(publicKey)
//                .zip().add(lz4)
//                .and()
//                .build()
//                .parseSignedClaims(compact);

//        Jwe<Claims> claimsJwe = Jwts.parser()
//                .verifyWith(publicKey)
//                .zip().add(lz4)
//                .and()
//                .decryptWith(privateKey)
//                .build()
//                .parseEncryptedClaims(compact);

//        JwsHeader header = claimsJws.getHeader();
//        String keyId = header.getKeyId();
//        String algorithm = header.getAlgorithm();
//        String compressionAlgorithmId = header.getCompressionAlgorithm();
//
//        Claims payload = claimsJws.getPayload();
//        Set<String> audience = payload.getAudience();
//        String subject = payload.getSubject();
//        String issuer = payload.getIssuer();
//        Date issuedAt = payload.getIssuedAt();
//        Date expiration = payload.getExpiration();

        KeyPair rsaKeyPair = new KeyPair(publicKey, privateKey);

        String rsaPrivateKeyJwk = jwtLoader.generateRsaPrivateKeyJwk((RSAPrivateKey) privateKey);
        RsaPrivateJwk parseRsaKeyJwk  = jwtLoader.parseRsaKeyJwk(rsaPrivateKeyJwk);

        System.out.println();


    }
}
