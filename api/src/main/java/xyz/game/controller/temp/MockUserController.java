package xyz.game.controller.temp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.AttributeService;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 属性表(Attribute)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping
public class MockUserController {

    @GetMapping("/users/info")
    public ResponseEntity<ResponseData<User>> userInfo(AttributeReq attribute) {
        System.out.println("进来了");
        ResponseData<User> resp = new ResponseData<User>();
        User user = new User();
        user.setUsername("admin");
        user.setRoles(Collections.singletonList("admin"));
        resp.setData(user);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/users/login")
    public ResponseEntity<ResponseData<Token>> login(AttributeReq attribute) {
        System.out.println("进来了");
        ResponseData<Token> resp = new ResponseData<Token>();
        Token token = new Token();
        token.setToken("666");
        resp.setData(token);
        return ResponseEntity.ok(resp);
    }

}

