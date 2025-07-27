package xyz.game.service.impl;

import xyz.game.dao.IndividualDao;
import xyz.game.entity.IndivName;
import xyz.game.dao.IndivNameDao;
import xyz.game.entity.Individual;
import xyz.game.service.IndivNameService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (IndivName)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("indivNameService")
public class IndivNameServiceImpl implements IndivNameService {
    private final IndivNameDao indivNameDao;

    private final IndividualDao individualDao;

    public IndivNameServiceImpl(IndivNameDao indivNameDao, IndividualDao individualDao) {
        this.indivNameDao = indivNameDao;
        this.individualDao = individualDao;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param indivId 主键
     * @return 实例对象
     */
    @Override
    public IndivName queryById(Integer indivId) {
        return this.indivNameDao.queryById(indivId);
    }

    /**
     * 分页查询
     *
     * @param indivName 筛选条件
     * @return 查询结果
     */
    @Override
    public List<IndivName> query(IndivName indivName) {
        List<IndivName> query = this.indivNameDao.query(indivName);
        List<IndivName> queryImg = this.indivNameDao.queryImg(indivName);
        Map<Integer, String> map = new HashMap<>();
        queryImg.forEach(name -> map.put(name.getIndivId(), name.getIndivImg()));
        for (IndivName item : query) {
            item.setIndivImg(map.get(item.getIndivId()));
        }
        return query;
    }

    /**
     * 新增数据
     *
     * @param indivName 实例对象
     * @return 实例对象
     */
    @Override
    public IndivName insert(IndivName indivName) {
        Integer id = this.indivNameDao.maxId();
        if (id == null) {
            id = 1;
        } else {
            id += 1;
        }
        indivName.setIndivId(id);
        this.indivNameDao.insert(indivName);
        return indivName;
    }

    /**
     * 修改数据
     *
     * @param indivName 实例对象
     * @return 实例对象
     */
    @Override
    public IndivName update(IndivName indivName) {
        this.indivNameDao.update(indivName);
        Individual individual = new Individual();
        individual.setIndivId(indivName.getIndivId());
        individual.setIndivImg(indivName.getIndivImg());
        this.individualDao.update(individual);
        return this.queryById(indivName.getIndivId());
    }

    /**
     * 通过主键删除数据
     *
     * @param indivId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer indivId) {
        return this.indivNameDao.deleteById(indivId) > 0;
    }
}
