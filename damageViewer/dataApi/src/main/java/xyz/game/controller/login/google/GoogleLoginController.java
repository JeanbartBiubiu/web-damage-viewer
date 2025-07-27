package xyz.game.controller.login.google;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.game.controller.global.ResponseData;
import xyz.game.controller.login.SSOBody;
import xyz.game.entity.custom.JwtBody;
import xyz.game.service.EmailService;

/**
 * 属性表(Attribute)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("/login/google")
public class GoogleLoginController {
    /**
     * 服务对象
     */
    private final EmailService emailService;


    public GoogleLoginController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     *  获取token
     */
    @PostMapping("token")
    public ResponseEntity<ResponseData<String>> query(@RequestBody SSOBody ssoBody) {
        ResponseData<String> resp = new ResponseData<>();
        JwtBody jwtBody = emailService.googleLogin(ssoBody.getJwt());
        if (jwtBody == null) {
            return ResponseEntity.badRequest().build();
        }
        resp.setData(jwtBody.getToken());
        return ResponseEntity.ok(resp);
    }

}

