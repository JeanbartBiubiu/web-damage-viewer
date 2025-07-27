package xyz.game.service.impl;

import xyz.game.entity.IndivSkill;
import xyz.game.dao.IndivSkillDao;
import xyz.game.service.IndivSkillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * 人物-技能关联表(IndivSkill)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("indivSkillService")
public class IndivSkillServiceImpl implements IndivSkillService {
    @Resource
    private IndivSkillDao indivSkillDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public IndivSkill queryById( ) {
        return this.indivSkillDao.queryById();
    }

    /**
     * 分页查询
     *
     * @param indivSkill 筛选条件
     * @return 查询结果
     */
    @Override
    public List<IndivSkill> query(IndivSkill indivSkill) {
        return this.indivSkillDao.query(indivSkill);
    }

    /**
     * 新增数据
     *
     * @param indivSkill 实例对象
     * @return 实例对象
     */
    @Override
    public IndivSkill insert(IndivSkill indivSkill) {
        this.indivSkillDao.insert(indivSkill);
        return indivSkill;
    }

    /**
     * 修改数据
     *
     * @param indivSkill 实例对象
     * @return 实例对象
     */
    @Override
    public IndivSkill update(IndivSkill indivSkill) {
        this.indivSkillDao.update(indivSkill);
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
        return this.indivSkillDao.deleteById() > 0;
    }
}
