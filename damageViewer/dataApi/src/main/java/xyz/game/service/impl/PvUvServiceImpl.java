package xyz.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.game.dao.PvUvMapper;
import xyz.game.entity.PvUv;
import xyz.game.service.PvUvService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PvUvServiceImpl extends ServiceImpl<PvUvMapper, PvUv> implements PvUvService {

    @Override
    public void savePvUvData(Long hour, Long pv, Long uv) {
        PvUv hour1 = this.getOne(new QueryWrapper<PvUv>().ge("hour", hour));
        if (hour1 != null) {
            return;
        }
        PvUv pvUv = new PvUv();
        pvUv.setHour(hour);
        pvUv.setPv(pv);
        pvUv.setUv(uv);
        this.save(pvUv);
    }

    @Override
    @Cacheable(value = "pvUvLast30DaysCache", key = "#root.methodName")
    public List<PvUv> getLast30DaysData() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        // 这里需要根据实际的数据库操作框架和 PvUvMapper 来实现具体的查询逻辑
        // 示例中假设 PvUvMapper 有一个方法来查询近 30 天的数据
        return this.baseMapper.selectList(
                new QueryWrapper<PvUv>()
                        .ge("hour", thirtyDaysAgo.toEpochSecond(ZoneOffset.UTC)/3600000)
        );
    }
}