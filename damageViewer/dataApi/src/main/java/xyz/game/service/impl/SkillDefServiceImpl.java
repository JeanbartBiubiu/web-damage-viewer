package xyz.game.service.impl;

import xyz.game.entity.SkillDef;
import xyz.game.dao.SkillDefDao;
import xyz.game.service.SkillDefService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * 技能定义表(SkillDef)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("skillDefService")
public class SkillDefServiceImpl implements SkillDefService {
    @Resource
    private SkillDefDao skillDefDao;

    /**
     * 通过ID查询单条数据
     *
     * @param skillId 主键
     * @return 实例对象
     */
    @Override
    public SkillDef queryById(Integer skillId) {
        return this.skillDefDao.queryById(skillId);
    }

    /**
     * 分页查询
     *
     * @param skillDef 筛选条件
     * @return 查询结果
     */
    @Override
    public List<SkillDef> query(SkillDef skillDef) {
        return this.skillDefDao.query(skillDef);
    }

    /**
     * 新增数据
     *
     * @param skillDef 实例对象
     * @return 实例对象
     */
    @Override
    public SkillDef insert(SkillDef skillDef) {
        this.skillDefDao.insert(skillDef);
        return skillDef;
    }

    /**
     * 修改数据
     *
     * @param skillDef 实例对象
     * @return 实例对象
     */
    @Override
    public SkillDef update(SkillDef skillDef) {
        this.skillDefDao.update(skillDef);
        return this.queryById(skillDef.getSkillId());
    }

    /**
     * 通过主键删除数据
     *
     * @param skillId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer skillId) {
        return this.skillDefDao.deleteById(skillId) > 0;
    }
}
