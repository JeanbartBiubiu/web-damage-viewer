package xyz.game.dao;

import xyz.game.entity.ComputingLifeCycle;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (ComputingLifeCycle)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface ComputingLifeCycleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param cycleId 主键
     * @return 实例对象
     */
    ComputingLifeCycle queryById(Integer cycleId);

    /**
     * 查询指定行数据
     *
     * @param computingLifeCycle 查询条件
     * @return 对象列表
     */
    List<ComputingLifeCycle> query(ComputingLifeCycle computingLifeCycle);

    /**
     * 统计总行数
     *
     * @param computingLifeCycle 查询条件
     * @return 总行数
     */
    long count(ComputingLifeCycle computingLifeCycle);

    /**
     * 新增数据
     *
     * @param computingLifeCycle 实例对象
     * @return 影响行数
     */
    int insert(ComputingLifeCycle computingLifeCycle);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ComputingLifeCycle> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ComputingLifeCycle> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ComputingLifeCycle> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ComputingLifeCycle> entities);

    /**
     * 修改数据
     *
     * @param computingLifeCycle 实例对象
     * @return 影响行数
     */
    int update(ComputingLifeCycle computingLifeCycle);

    /**
     * 通过主键删除数据
     *
     * @param cycleId 主键
     * @return 影响行数
     */
    int deleteById(Integer cycleId);

}

