package xyz.game.controller.cache;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.game.controller.global.ResponseData;
import xyz.game.util.RedisUtil;
import xyz.game.util.ThreadLocalUtil;

@RestController
@RequestMapping("/cache")
public class AllInfo {
    private final RedisUtil redisUtil;

    public AllInfo(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 获取所有信息
     */
    @RequestMapping("/getAllInfo")
    public ResponseEntity<ResponseData<String>> getAllInfo() {
        // TODO 数据生成服务，定时任务or某些条件自动触发
        String currentSchema = ThreadLocalUtil.getOrDefault("currentSchema","default_table");
        System.out.println(currentSchema + "_allInfo");
        String s = redisUtil.get(currentSchema + "_allInfo");
        ResponseData<String> resp = new ResponseData<>();
        resp.setData(s);
        return ResponseEntity.ok(resp);
    }


}
