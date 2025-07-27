package xyz.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.game.entity.EquipId;
import org.apache.ibatis.annotations.Param;
import xyz.game.entity.PvUv;

import java.util.List;

/**
 * (EquipId)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface EquipIdDao  extends BaseMapper<EquipId> {
}

