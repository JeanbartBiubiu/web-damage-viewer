package xyz.game.service;

import xyz.game.entity.FormulaImpl;

import java.util.List;

/**
 * (FormulaImpl)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface FormulaImplService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    FormulaImpl queryById( );

    /**
     * 分页查询
     *
     * @param formulaImpl 筛选条件
     * @return 查询结果
     */
    List<FormulaImpl> query(FormulaImpl formulaImpl);

    /**
     * 新增数据
     *
     * @param formulaImpl 实例对象
     * @return 实例对象
     */
    FormulaImpl insert(FormulaImpl formulaImpl);

    /**
     * 修改数据
     *
     * @param formulaImpl 实例对象
     * @return 实例对象
     */
    FormulaImpl update(FormulaImpl formulaImpl);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById( );

}
