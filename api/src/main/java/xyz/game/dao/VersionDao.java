package xyz.game.dao;

import xyz.game.entity.Version;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 版本记录(Version)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface VersionDao {

    /**
     * @return 实例对象
     */
    Version queryNewest();

    /**
     * 新增数据
     *
     * @param version 实例对象
     * @return 影响行数
     */
    int insert(Version version);

    /**
     * 修改数据
     *
     * @param version 实例对象
     * @return 影响行数
     */
    int update(Version version);

    /**
     * 通过主键删除数据
     *
     * @param versionId 主键
     * @return 影响行数
     */
    int deleteById(Integer versionId);

}

