package xyz.game.dao;

import xyz.game.entity.AttributeName;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 属性名称表(AttributeName)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface AttributeNameDao {

    /**
     * 通过ID查询单条数据
     *
     * @param attributeId 主键
     * @return 实例对象
     */
    AttributeName queryById(Integer attributeId);

    /**
     * 查询指定行数据
     *
     * @param attributeName 查询条件
     * @return 对象列表
     */
    List<AttributeName> query(AttributeName attributeName);

    /**
     * 统计总行数
     *
     * @param attributeName 查询条件
     * @return 总行数
     */
    long count(AttributeName attributeName);

    /**
     * 新增数据
     *
     * @param attributeName 实例对象
     * @return 影响行数
     */
    int insert(AttributeName attributeName);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AttributeName> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AttributeName> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AttributeName> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<AttributeName> entities);

    /**
     * 修改数据
     *
     * @param attributeName 实例对象
     * @return 影响行数
     */
    int update(AttributeName attributeName);

    /**
     * 通过主键删除数据
     *
     * @param attributeId 主键
     * @return 影响行数
     */
    int deleteById(Integer attributeId);

}

