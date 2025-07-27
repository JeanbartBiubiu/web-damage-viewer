package xyz.game.service.impl;

import xyz.game.entity.FormulaDefault;
import xyz.game.dao.FormulaDefaultDao;
import xyz.game.service.FormulaDefaultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (FormulaDefault)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("formulaDefaultService")
public class FormulaDefaultServiceImpl implements FormulaDefaultService {
    @Resource
    private FormulaDefaultDao formulaDefaultDao;

    /**
     * 通过ID查询单条数据
     *
     * @param formulaId 主键
     * @return 实例对象
     */
    @Override
    public FormulaDefault queryById(Integer formulaId) {
        return this.formulaDefaultDao.queryById(formulaId);
    }

    /**
     * 分页查询
     *
     * @param formulaDefault 筛选条件
     * @return 查询结果
     */
    @Override
    public List<FormulaDefault> query(FormulaDefault formulaDefault) {
        return this.formulaDefaultDao.query(formulaDefault);
    }

    /**
     * 新增数据
     *
     * @param formulaDefault 实例对象
     * @return 实例对象
     */
    @Override
    public FormulaDefault insert(FormulaDefault formulaDefault) {
        this.formulaDefaultDao.insert(formulaDefault);
        return formulaDefault;
    }

    /**
     * 修改数据
     *
     * @param formulaDefault 实例对象
     * @return 实例对象
     */
    @Override
    public FormulaDefault update(FormulaDefault formulaDefault) {
        this.formulaDefaultDao.update(formulaDefault);
        return this.queryById(formulaDefault.getFormulaId());
    }

    /**
     * 通过主键删除数据
     *
     * @param formulaId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer formulaId) {
        return this.formulaDefaultDao.deleteById(formulaId) > 0;
    }
}
