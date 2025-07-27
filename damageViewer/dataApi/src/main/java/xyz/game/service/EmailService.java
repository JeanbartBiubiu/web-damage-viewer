package xyz.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.game.entity.Email;
import xyz.game.entity.PvUv;
import xyz.game.entity.custom.JwtBody;

import java.util.List;

public interface EmailService extends IService<Email> {
    JwtBody huaweiLogin(String jwt);

    JwtBody googleLogin(String jwt);

}