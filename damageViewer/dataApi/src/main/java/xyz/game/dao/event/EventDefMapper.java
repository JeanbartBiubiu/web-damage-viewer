package xyz.game.dao.event;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.game.function.event.entity.EventDef;

@Mapper
public interface EventDefMapper extends BaseMapper<EventDef> {
}
