package xyz.game.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 属性表(Attribute)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface SchemaDao {
    void chooseSchema(@Param("currentSchema") String currentSchema);
}

