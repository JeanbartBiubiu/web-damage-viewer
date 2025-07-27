package xyz.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.game.function.event.entity.EventDef;
import xyz.game.function.image.Image;

@Mapper
public interface ImageDao extends BaseMapper<Image> {
}
