package xyz.game.controller;

import xyz.game.service.VersionService;
import org.springframework.web.bind.annotation.*;
import xyz.game.util.RedisUtil;

/**
 * 版本记录(Version)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("version")
public class VersionController {
    /**
     * 服务对象
     */
    private final VersionService versionService;
    private final RedisUtil redisUtil;

    public VersionController(VersionService versionService, RedisUtil redisUtil) {
        this.versionService = versionService;
        this.redisUtil = redisUtil;
    }

    @GetMapping("test")
    public String test(){
        return redisUtil.get("test");
    }

}

