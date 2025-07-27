package xyz.game.service.impl;

import xyz.game.entity.FormulaImpl;
import xyz.game.dao.FormulaImplDao;
import xyz.game.service.FormulaImplService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (FormulaImpl)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("formulaImplService")
public class FormulaImplServiceImpl implements FormulaImplService {
    @Resource
    private FormulaImplDao formulaImplDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public FormulaImpl queryById( ) {
        return this.formulaImplDao.queryById();
    }

    /**
     * 分页查询
     *
     * @param formulaImpl 筛选条件
     * @return 查询结果
     */
    @Override
    public List<FormulaImpl> query(FormulaImpl formulaImpl) {
        return this.formulaImplDao.query(formulaImpl);
    }

    /**
     * 新增数据
     *
     * @param formulaImpl 实例对象
     * @return 实例对象
     */
    @Override
    public FormulaImpl insert(FormulaImpl formulaImpl) {
        this.formulaImplDao.insert(formulaImpl);
        return formulaImpl;
    }

    /**
     * 修改数据
     *
     * @param formulaImpl 实例对象
     * @return 实例对象
     */
    @Override
    public FormulaImpl update(FormulaImpl formulaImpl) {
        this.formulaImplDao.update(formulaImpl);
        return this.queryById();
    }

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.formulaImplDao.deleteById() > 0;
    }
}
