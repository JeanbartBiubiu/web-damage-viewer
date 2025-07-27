package xyz.game.dao;

import xyz.game.entity.FormulaImpl;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (FormulaImpl)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface FormulaImplDao {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    FormulaImpl queryById( );

    /**
     * 查询指定行数据
     *
     * @param formulaImpl 查询条件
     * @return 对象列表
     */
    List<FormulaImpl> query(FormulaImpl formulaImpl);

    /**
     * 统计总行数
     *
     * @param formulaImpl 查询条件
     * @return 总行数
     */
    long count(FormulaImpl formulaImpl);

    /**
     * 新增数据
     *
     * @param formulaImpl 实例对象
     * @return 影响行数
     */
    int insert(FormulaImpl formulaImpl);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<FormulaImpl> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<FormulaImpl> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FormulaImpl> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<FormulaImpl> entities);

    /**
     * 修改数据
     *
     * @param formulaImpl 实例对象
     * @return 影响行数
     */
    int update(FormulaImpl formulaImpl);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById( );

}

