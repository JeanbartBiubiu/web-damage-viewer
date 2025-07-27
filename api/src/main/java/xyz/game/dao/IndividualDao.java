package xyz.game.dao;

import xyz.game.entity.Individual;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Individual)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface IndividualDao {

    /**
     * 通过ID查询单条数据
     *
     * @param indivId 主键
     * @return 实例对象
     */
    Individual queryById(Integer indivId);

    /**
     * 查询指定行数据
     *
     * @param individual 查询条件
     * @return 对象列表
     */
    List<Individual> query(Individual individual);

    /**
     * 统计总行数
     *
     * @param individual 查询条件
     * @return 总行数
     */
    long count(Individual individual);

    /**
     * 新增数据
     *
     * @param individual 实例对象
     * @return 影响行数
     */
    int insert(Individual individual);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Individual> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Individual> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Individual> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Individual> entities);

    /**
     * 修改数据
     *
     * @param individual 实例对象
     * @return 影响行数
     */
    int update(Individual individual);

    /**
     * 通过主键删除数据
     *
     * @param indivId 主键
     * @return 影响行数
     */
    int deleteById(Integer indivId);

}

