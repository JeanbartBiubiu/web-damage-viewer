package xyz.game.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.PvUv;
import xyz.game.service.PvUvService;
import xyz.game.util.RedisUtil;

import java.util.List;

@RestController
@RequestMapping("/pv")
public class PVController {
    private final RedisUtil redisUtil;
    private final PvUvService pvUvService;

    public PVController(RedisUtil redisUtil, PvUvService pvUvService) {
        this.redisUtil = redisUtil;
        this.pvUvService = pvUvService;
    }

    @GetMapping
    public ResponseEntity<ResponseData<String>> query(@Param("uid")String uid) {
        ResponseData<String> resp = new ResponseData<>();
        redisUtil.increment(RedisUtil.PV);
        redisUtil.addSet(RedisUtil.UV,uid);
        resp.setData("ok");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/last30days")
    public ResponseEntity<ResponseData<List<PvUv>>> getLast30DaysData() {
        ResponseData<List<PvUv>> resp = new ResponseData<>();
        List<PvUv> data = pvUvService.getLast30DaysData();
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }

}
