package com.example.util;

import com.example.model.EncryptedData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class CryptoUtil {
    private static final Logger logger = LoggerFactory.getLogger(CryptoUtil.class);
    private static final String CURVE_NAME = "secp256k1";
    private static final String AES_ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final String PROVIDER = "BC";

    @Value("${crypto.private.key}")
    private String privateKeyHex;

    private ECPrivateKey privateKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            // 添加 BouncyCastle 提供者
            Security.addProvider(new BouncyCastleProvider());

            // 将十六进制私钥转换为 PKCS#8 格式
            byte[] pkcs8Bytes = KeyConverter.convertHexToPKCS8(privateKeyHex);

            // 使用 PKCS#8 格式初始化私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("EC", PROVIDER);
            privateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);

            logger.info("CryptoUtil initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize CryptoUtil", e);
            throw new RuntimeException("Failed to initialize encryption utilities", e);
        }
    }

    public String decrypt(EncryptedData encryptedData) {
        try {
            if (encryptedData == null) {
                throw new IllegalArgumentException("Encrypted data cannot be null");
            }
            if (encryptedData.getEphemeralPublicKey() == null || encryptedData.getIv() == null || encryptedData.getData() == null) {
                throw new IllegalArgumentException("Encrypted data fields cannot be null");
            }

            // 1. 使用临时公钥和服务器私钥计算共享密钥
            byte[] sharedSecret = calculateSharedSecret(encryptedData.getEphemeralPublicKey());

            // 2. 使用共享密钥和IV解密数据
            byte[] decryptedBytes = decryptWithAES(
                Base64.getDecoder().decode(encryptedData.getData()),
                sharedSecret,
                Base64.getDecoder().decode(encryptedData.getIv())
            );

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Decryption failed", e);
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }

    private byte[] calculateSharedSecret(String ephemeralPublicKeyHex) throws Exception {
        byte[] ephemeralPublicKeyBytes = hexStringToByteArray(ephemeralPublicKeyHex);
        java.security.spec.ECPoint point = java.security.spec.ECPoint.Factory.getInstance()
            .decode(ephemeralPublicKeyBytes);
        java.security.spec.ECParameterSpec params = privateKey.getParams();
        java.security.spec.ECPoint sharedPoint = point.multiply(privateKey.getS(), params.getCurve());
        return sharedPoint.getEncoded(false);
    }

    private byte[] decryptWithAES(byte[] encryptedData, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        return cipher.doFinal(encryptedData);
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public Map<String, Object> parseEncryptedData(String decryptedJson) {
        try {
            return objectMapper.readValue(decryptedJson, Map.class);
        } catch (Exception e) {
            logger.error("Failed to parse decrypted JSON data", e);
            throw new RuntimeException("Failed to parse decrypted data", e);
        }
    }
}
