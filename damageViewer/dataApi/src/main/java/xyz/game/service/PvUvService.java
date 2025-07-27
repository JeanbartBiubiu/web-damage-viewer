package xyz.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.game.entity.PvUv;

import java.util.List;

public interface PvUvService extends IService<PvUv> {
    void savePvUvData(Long hour, Long pv, Long uv);

    List<PvUv> getLast30DaysData();
}