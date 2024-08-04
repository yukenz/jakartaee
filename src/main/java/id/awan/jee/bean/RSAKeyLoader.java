package id.awan.jee.bean;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@ApplicationScoped
public class RSAKeyLoader {

    private final KeyFactory rsaKeyFactory;


    public RSAKeyLoader() {
        try {
            this.rsaKeyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey privateKey(byte[] privateKeyBytes) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return rsaKeyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey publicKey(byte[] publicKeyBytes) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return rsaKeyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
