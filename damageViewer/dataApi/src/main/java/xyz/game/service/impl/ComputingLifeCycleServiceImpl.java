package xyz.game.service.impl;

import xyz.game.entity.ComputingLifeCycle;
import xyz.game.dao.ComputingLifeCycleDao;
import xyz.game.service.ComputingLifeCycleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (ComputingLifeCycle)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("computingLifeCycleService")
public class ComputingLifeCycleServiceImpl implements ComputingLifeCycleService {
    @Resource
    private ComputingLifeCycleDao computingLifeCycleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param cycleId 主键
     * @return 实例对象
     */
    @Override
    public ComputingLifeCycle queryById(Integer cycleId) {
        return this.computingLifeCycleDao.queryById(cycleId);
    }

    /**
     * 分页查询
     *
     * @param computingLifeCycle 筛选条件
     * @return 查询结果
     */
    @Override
    public List<ComputingLifeCycle> query(ComputingLifeCycle computingLifeCycle) {
        return this.computingLifeCycleDao.query(computingLifeCycle);
    }

    /**
     * 新增数据
     *
     * @param computingLifeCycle 实例对象
     * @return 实例对象
     */
    @Override
    public ComputingLifeCycle insert(ComputingLifeCycle computingLifeCycle) {
        this.computingLifeCycleDao.insert(computingLifeCycle);
        return computingLifeCycle;
    }

    /**
     * 修改数据
     *
     * @param computingLifeCycle 实例对象
     * @return 实例对象
     */
    @Override
    public ComputingLifeCycle update(ComputingLifeCycle computingLifeCycle) {
        this.computingLifeCycleDao.update(computingLifeCycle);
        return this.queryById(computingLifeCycle.getCycleId());
    }

    /**
     * 通过主键删除数据
     *
     * @param cycleId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer cycleId) {
        return this.computingLifeCycleDao.deleteById(cycleId) > 0;
    }
}
