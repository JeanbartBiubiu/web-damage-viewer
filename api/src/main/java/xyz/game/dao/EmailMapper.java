package xyz.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.game.entity.Email;
import xyz.game.entity.PvUv;

@Mapper
public interface EmailMapper extends BaseMapper<Email> {
}