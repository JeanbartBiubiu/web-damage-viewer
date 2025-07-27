package xyz.game.util;

import lombok.Data;
import lombok.Getter;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import xyz.game.controller.global.EncryptedData;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class CryptoUtil {
    private static final String ALGORITHM_EC = "EC";
    private static final String ALGORITHM_ECDH = "ECDH";
    private static final String ALGORITHM_AES = "AES";
    private static final String AES_CIPHER_TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String PROVIDER_BC = "BC";
    private ECPrivateKey privateKey;

    @PostConstruct
    public void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        // 这里需要替换为实际的私钥十六进制字符串
        String privateKeyHex = "d9e67412fb3a7cb96e4ba8606b4fe44f8356113f8792e78491941322ddca95b1";
        byte[] privateKeyBytes = convertHexToPKCS8(privateKeyHex);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_EC, PROVIDER_BC);
        privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public String decrypt(EncryptedData encryptedData) throws GeneralSecurityException {
        byte[] ephemeralPublicKeyBytes = convertHexToPKCS8(encryptedData.getEphemeralPublicKey());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(ephemeralPublicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_EC, PROVIDER_BC);
        PublicKey ephemeralPublicKey = keyFactory.generatePublic(publicKeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance(ALGORITHM_ECDH, PROVIDER_BC);
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(ephemeralPublicKey, true);
        byte[] sharedSecret = keyAgreement.generateSecret();

        byte[] iv = Base64.getDecoder().decode(encryptedData.getIv());
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData.getData());

        SecretKeySpec secretKey = new SecretKeySpec(sharedSecret, ALGORITHM_AES);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(AES_CIPHER_TRANSFORMATION, PROVIDER_BC);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static byte[] convertHexToPKCS8(String privateKeyHex) {
        try {
            // 1. 创建 EC 私钥结构
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new ASN1Integer(1)); // version
            v.add(new DEROctetString(Hex.decode(privateKeyHex))); // private key
            v.add(new DERBitString(new byte[0])); // optional parameters
            v.add(new DERBitString(new byte[0])); // optional public key

            ASN1Sequence ecPrivateKey = new DERSequence(v);

            // 2. 创建算法标识符
            AlgorithmIdentifier algId = new AlgorithmIdentifier(
                    X9ObjectIdentifiers.id_ecPublicKey,
                    SECObjectIdentifiers.secp256k1
            );

            // 3. 创建完整的 PKCS#8 结构
            ASN1EncodableVector v2 = new ASN1EncodableVector();
            v2.add(new ASN1Integer(0)); // version
            v2.add(algId);
            v2.add(new DEROctetString(ecPrivateKey.getEncoded()));
            ASN1Sequence seq = new DERSequence(v2);

            return seq.getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert private key", e);
        }
    }

}