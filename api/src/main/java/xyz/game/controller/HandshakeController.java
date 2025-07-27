package xyz.game.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.game.controller.global.EncryptedData;
import xyz.game.controller.global.ResponseData;
import xyz.game.util.CryptoUtil;

import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/handshake")
public class HandshakeController {

    @Autowired
    private CryptoUtil cryptoUtil;

    @PostMapping("/getAToken")
    public ResponseEntity<ResponseData<String>> getAToken(@RequestBody GetTokenRequest request) {
        ResponseData<String> responseData = new ResponseData<>();
        try {
            String decryptedData = cryptoUtil.decrypt(request.getProof());
            System.out.println(decryptedData);
            // 这里可以对解密后的数据进行验证，例如检查时间戳等
            // 模拟生成 Token
            String token = "generated_token_123456";
            responseData.setCode(200);
            responseData.setMessage("Token 获取成功");
            responseData.setData(token);
            return ResponseEntity.ok(responseData);
        } catch (GeneralSecurityException e) {
            responseData.setCode(500);
            responseData.setMessage("解密数据失败");
            return ResponseEntity.status(500).body(responseData);
        }
    }
}

@Data
class GetTokenRequest {
    private EncryptedData proof;
}