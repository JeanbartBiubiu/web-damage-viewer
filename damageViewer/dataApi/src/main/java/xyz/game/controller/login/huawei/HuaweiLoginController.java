package xyz.game.controller.login.huawei;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.controller.login.SSOBody;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.entity.custom.JwtBody;
import xyz.game.service.EmailService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 属性表(Attribute)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("/login/huawei")
public class HuaweiLoginController {
    /**
     * 服务对象
     */
    private final EmailService emailService;

    private static final PrivateKey PRIVATE_KEY;

    static {
        try {
            // 加载资源文件
            ClassPathResource resource = new ClassPathResource("token-private-key.txt");
            StringBuilder keyContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    keyContent.append(line);
                }
            }

            // 去除 PEM 格式的头尾
            String privateKeyPem = keyContent.toString()
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            // 解码 Base64 字符串
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PRIVATE_KEY = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize private key", e);
        }
    }


    public HuaweiLoginController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * 获取token
     */
    @PostMapping("token")
    public ResponseEntity<ResponseData<String>> query(@RequestBody SSOBody ssoBody) {
        ResponseData<String> resp = new ResponseData<>();
        JwtBody jwtBody = emailService.huaweiLogin(ssoBody.getJwt());
        if (jwtBody == null) {
            return ResponseEntity.badRequest().build();
        }

        resp.setData(generateJwtToken(jwtBody));
        return ResponseEntity.ok(resp);
    }

    private String generateJwtToken(JwtBody jwtBody) {
        return Jwts.builder()
                .setSubject(jwtBody.getToken())
                .claim("canEdit", jwtBody.getCanEdit())
                .claim("pay", jwtBody.getPay())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000 * 24 * 14)) // 10 天有效期
                .signWith(PRIVATE_KEY, SignatureAlgorithm.RS256)
                .compact();
    }

}

