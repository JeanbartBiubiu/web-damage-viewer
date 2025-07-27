package xyz.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.agconnect.server.commons.exception.AGCException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import xyz.game.dao.EmailMapper;
import xyz.game.entity.Email;
import xyz.game.entity.custom.JwtBody;
import xyz.game.service.EmailService;
import xyz.game.util.RedisUtil;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Service
public class EmailServiceImpl extends ServiceImpl<EmailMapper, Email> implements EmailService {
    private final String HuaweiPublicKey = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAius4X2mlTbLRt5yLcv/tZOUPp9i95Ap7X+ID9sB+30byXbL4P7FBuPyhR6fEKiZezXI9l8c3g5sZniU0P1mWxoCbiE8LmM3X0m0/0fS4VsE8RLWRLgrVQL6bBAZnzlOPYKWX0inz1CCOh3mY+SUQciseXTx13LzYDYbWzNSuQ6l5p0ajkRefEE+a3LNg43nQyaU0S/7gLBCZ2Iqyv3ppLTWMTPThuzX6DxRHEh7cye1XDUW0e7LCQnKuX1hERmPgzwBzRByLJ8NMRmZTcrFXa+Uph0p3JITGHLV+AX2ur1i01RFQnipHZ8Ga68Y2rDqsxBPLb8pHCc3alxeB8tLhQPoQmqHx6VlRZbGYom6fjnMWQw3qGSnHD6TMZlDiwy94r6ARq6dm+xNlZ347f7e7h9v0racIBXkQsCABHYYRPP9Jcn2jD6Y74qT47xGz5yr1O6vQqYfKVNMPcD4cB+uOGlr2lqjEOM93c/PkMbzaP7UhvEVd+WHzV9HbNVgDfSIxAgMBAAE=";
    private final String HuaweiProductId = "461323198429888697";
    private final String GoogleProductId = "281073904047-bjv1dcnups9jkdnqb94t66stcve0smi9.apps.googleusercontent.com";
    private final RedisUtil redisUtil;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public EmailServiceImpl(RedisUtil redisUtil) throws AGCException {
        this.redisUtil = redisUtil;
    }

    @Override
    public JwtBody huaweiLogin(String jwt) {
        String email = verifyHuaweiJwt(jwt, HuaweiProductId, HuaweiPublicKey);
        if (!Strings.isEmpty(email)) {
            // 更新token
            Email dbRecord = this.getOne(new QueryWrapper<Email>().eq("email_address", email));
            if (dbRecord == null) {
                // 新增记录
                dbRecord = new Email();
                dbRecord.setEmailAddress(email);
                dbRecord.setToken(createToken());
                dbRecord.setCanEdit(0);
                dbRecord.setPay(0);
                this.save(dbRecord);
            } else {
                // remove掉redis的token记录
                redisUtil.delete("token." + dbRecord.getToken());
                dbRecord.setToken(createToken());
                this.updateById(dbRecord);
            }
            redisUtil.setWithExpire("token." + dbRecord.getToken(), dbRecord.getCanEdit() + "" + dbRecord.getPay(), 3600 * 24 * 14);
            JwtBody jwtBody = new JwtBody();
            jwtBody.setToken(dbRecord.getToken());
            jwtBody.setCanEdit(dbRecord.getCanEdit());
            jwtBody.setPay(dbRecord.getPay());
            return jwtBody;
        }
        return null;
    }

    @Override
    public JwtBody googleLogin(String jwt) {
        /*boolean b = verifyGoogleJwt(token, GoogleProductId);
        if (b) {
            // 更新token
            Email dbRecord = this.getOne(new QueryWrapper<Email>().eq("email_address", email));
            if (dbRecord == null) {
                // 新增记录
                dbRecord = new Email();
                dbRecord.setEmailAddress(email);
                dbRecord.setToken(createToken());
                dbRecord.setCanEdit(0);
                dbRecord.setPay(0);
                this.save(dbRecord);
            } else {
                // remove掉redis的token记录
                redisUtil.delete("token." + dbRecord.getToken());
                dbRecord.setToken(createToken());
                this.updateById(dbRecord);
            }
            redisUtil.setWithExpire("token." + dbRecord.getToken(), "00", 3600 * 24 * 14);
            return dbRecord.getToken();
        }*/
        return null;
    }

    private String createToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private String verifyHuaweiJwt(String token, String productId, String pubKeyStr) {
        String[] field = token.split("\\.");
        if (field.length != 3) {
            return null;
        }
        String payLoadStr = new String(Base64.getUrlDecoder().decode(field[1]), StandardCharsets.UTF_8);
        JSONObject payLoad = JSON.parseObject(payLoadStr);
        long expTimeLong = payLoad.getLong("exp");
        Instant instant = Instant.ofEpochSecond(expTimeLong);
        LocalDateTime expTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        // 验证token是否已过期
        if (LocalDateTime.now(ZoneId.of("UTC")).isAfter(expTime)) {
            // return null;
        }
        // 验证是否是本项目的token
        if (!productId.equals(payLoad.getString("aud"))) {
            return null;
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pubKeyStr)));
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return payLoad.getString("email");
        } catch (Exception e) {
            return null;
        }
    }

    private boolean verifyGoogleJwt(String jwt, String productId) {
        String[] field = jwt.split("\\.");
        if (field.length != 3) {
            return false;
        }
        String headerStr = new String(Base64.getUrlDecoder().decode(field[0]), StandardCharsets.UTF_8);
        JSONObject header = JSON.parseObject(headerStr);
        String kid = header.getString("kid");
        String pk = redisUtil.get("googlePublicKey." + kid);
        if (pk == null) {
            return false;
        }

        String payLoadStr = new String(Base64.getUrlDecoder().decode(field[1]), StandardCharsets.UTF_8);
        JSONObject payLoad = JSON.parseObject(payLoadStr);
        long expTimeLong = payLoad.getLong("exp");
        Instant instant = Instant.ofEpochSecond(expTimeLong);
        LocalDateTime expTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        // 验证token是否已过期
        if (LocalDateTime.now(ZoneId.of("UTC")).isAfter(expTime)) {
            return false;
        }
        // 验证是否是本项目的token
        if (!productId.equals(payLoad.getString("aud"))) {
            return false;
        }
        try {
            // 每小时都要刷新token，服务器还连不上谷歌，先放着
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(pk.getBytes(StandardCharsets.UTF_8));
            X509Certificate cert = (X509Certificate) factory.generateCertificate(bais);
            PublicKey publicKey = cert.getPublicKey();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            System.out.println("User ID: " + claims.get("userId"));
            return true;
        } catch (JwtException e) {
            // 签名无效、过期等
            System.out.println("Invalid JWT: " + e.getMessage());
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
        return false;

    }
}