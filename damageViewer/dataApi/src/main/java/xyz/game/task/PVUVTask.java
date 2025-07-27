package xyz.game.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.game.service.PvUvService;
import xyz.game.util.RedisUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@DisallowConcurrentExecution
public class PVUVTask implements Job {

    private static final String LOCK_KEY = "pvuv_task_lock";
    private static final String LOCK_VALUE = "pvuv_task_value";
    private static final long LOCK_EXPIRE_TIME = 3600; // 锁的过期时间，单位：秒

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PvUvService pvUvService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (redisUtil.tryLock(LOCK_KEY, LOCK_VALUE, LOCK_EXPIRE_TIME, java.util.concurrent.TimeUnit.SECONDS)) {
            try {
                // 获取当前小时
                long hour = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) / 3600;

                // 获取PV和UV数据
                Long pv = redisUtil.get(RedisUtil.PV) == null ? 0 : Long.parseLong(redisUtil.get(RedisUtil.PV));
                Long uv = redisUtil.setCard(RedisUtil.UV);

                // 将数据存入数据库
                pvUvService.savePvUvData(hour, pv, uv);

                // 重置PV和UV数据
                redisUtil.set(RedisUtil.PV, "0");
                redisUtil.delete(RedisUtil.UV);
            } finally {
                // 释放锁
                redisUtil.unlock(LOCK_KEY);
            }
        }
    }
}