package xyz.game.service;

import xyz.game.entity.FormulaDefault;

import java.util.List;

/**
 * (FormulaDefault)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface FormulaDefaultService {

    /**
     * 通过ID查询单条数据
     *
     * @param formulaId 主键
     * @return 实例对象
     */
    FormulaDefault queryById(Integer formulaId);

    /**
     * 分页查询
     *
     * @param formulaDefault 筛选条件
     * @return 查询结果
     */
    List<FormulaDefault> query(FormulaDefault formulaDefault);

    /**
     * 新增数据
     *
     * @param formulaDefault 实例对象
     * @return 实例对象
     */
    FormulaDefault insert(FormulaDefault formulaDefault);

    /**
     * 修改数据
     *
     * @param formulaDefault 实例对象
     * @return 实例对象
     */
    FormulaDefault update(FormulaDefault formulaDefault);

    /**
     * 通过主键删除数据
     *
     * @param formulaId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer formulaId);

}
