package xyz.game.dao.event;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.game.function.event.entity.EventType;

@Mapper
public interface EventTypeMapper extends BaseMapper<EventType> {
}
