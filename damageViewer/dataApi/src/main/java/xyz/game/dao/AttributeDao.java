package xyz.game.dao;

import xyz.game.entity.Attribute;
import org.apache.ibatis.annotations.Param;
import xyz.game.entity.custom.AttributeReq;

import java.util.List;

/**
 * 属性表(Attribute)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface AttributeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param attributeId 主键
     * @return 实例对象
     */
    AttributeReq queryById(Integer attributeId);

    /**
     * 查询指定行数据
     *
     * @param attribute 查询条件
     * @return 对象列表
     */
    List<Attribute> query(Attribute attribute);


    List<AttributeReq> queryJoin(AttributeReq req);

    /**
     * 统计总行数
     *
     * @param attribute 查询条件
     * @return 总行数
     */
    long count(Attribute attribute);
    Integer maxId();

    /**
     * 新增数据
     *
     * @param attribute 实例对象
     * @return 影响行数
     */
    int insert(Attribute attribute);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Attribute> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Attribute> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Attribute> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Attribute> entities);

    /**
     * 修改数据
     *
     * @param attribute 实例对象
     * @return 影响行数
     */
    int update(Attribute attribute);

    /**
     * 通过主键删除数据
     *
     * @param attributeId 主键
     * @return 影响行数
     */
    int deleteById(Integer attributeId);

}

