package xyz.game.service.impl;

import xyz.game.entity.Individual;
import xyz.game.dao.IndividualDao;
import xyz.game.service.IndividualService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (Individual)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("individualService")
public class IndividualServiceImpl implements IndividualService {
    @Resource
    private IndividualDao individualDao;

    /**
     * 通过ID查询单条数据
     *
     * @param indivId 主键
     * @return 实例对象
     */
    @Override
    public Individual queryById(Integer indivId) {
        return this.individualDao.queryById(indivId);
    }

    /**
     * 分页查询
     *
     * @param individual 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Individual> query(Individual individual) {
        return this.individualDao.query(individual);
    }

    /**
     * 新增数据
     *
     * @param individual 实例对象
     * @return 实例对象
     */
    @Override
    public Individual insert(Individual individual) {
        this.individualDao.insert(individual);
        return individual;
    }

    /**
     * 修改数据
     *
     * @param individual 实例对象
     * @return 实例对象
     */
    @Override
    public Individual update(Individual individual) {
        this.individualDao.update(individual);
        return this.queryById(individual.getIndivId());
    }

    /**
     * 通过主键删除数据
     *
     * @param indivId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer indivId) {
        return this.individualDao.deleteById(indivId) > 0;
    }
}
