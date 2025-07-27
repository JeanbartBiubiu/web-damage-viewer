package xyz.game.dao;

import xyz.game.entity.AttributeValue;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 单位-属性-等级对应数据数值(AttributeValue)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface AttributeValueDao {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    List<AttributeValue> queryById(@Param("indivId") int indivId);

    /**
     * 查询指定行数据
     *
     * @param attributeValue 查询条件
     * @return 对象列表
     */
    List<AttributeValue> query(AttributeValue attributeValue);

    /**
     * 统计总行数
     *
     * @param attributeValue 查询条件
     * @return 总行数
     */
    long count(AttributeValue attributeValue);

    /**
     * 新增数据
     *
     * @param attributeValue 实例对象
     * @return 影响行数
     */
    int insert(AttributeValue attributeValue);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AttributeValue> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AttributeValue> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AttributeValue> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<AttributeValue> entities);

    /**
     * 修改数据
     *
     * @param attributeValue 实例对象
     * @return 影响行数
     */
    int update(AttributeValue attributeValue);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById();

    int deleteByIndivId(@Param("id")int id);

}

