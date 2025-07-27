package xyz.game.service;

import xyz.game.entity.ComputingLifeCycle;

import java.util.List;

/**
 * (ComputingLifeCycle)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface ComputingLifeCycleService {

    /**
     * 通过ID查询单条数据
     *
     * @param cycleId 主键
     * @return 实例对象
     */
    ComputingLifeCycle queryById(Integer cycleId);

    /**
     * 分页查询
     *
     * @param computingLifeCycle 筛选条件
     * @return 查询结果
     */
    List<ComputingLifeCycle> query(ComputingLifeCycle computingLifeCycle);

    /**
     * 新增数据
     *
     * @param computingLifeCycle 实例对象
     * @return 实例对象
     */
    ComputingLifeCycle insert(ComputingLifeCycle computingLifeCycle);

    /**
     * 修改数据
     *
     * @param computingLifeCycle 实例对象
     * @return 实例对象
     */
    ComputingLifeCycle update(ComputingLifeCycle computingLifeCycle);

    /**
     * 通过主键删除数据
     *
     * @param cycleId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer cycleId);

}
