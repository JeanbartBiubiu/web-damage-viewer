package xyz.game.service.impl;

import xyz.game.entity.SkillSub;
import xyz.game.dao.SkillSubDao;
import xyz.game.entity.custom.SkillReq;
import xyz.game.service.SkillSubService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (SkillSub)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("skillSubService")
public class SkillSubServiceImpl implements SkillSubService {
    @Resource
    private SkillSubDao skillSubDao;

    /**
     * 通过ID查询单条数据
     *
     * @param skillId 主键
     * @return 实例对象
     */
    @Override
    public SkillSub queryById(Integer skillId) {
        return this.skillSubDao.queryById(skillId);
    }

    /**
     * 分页查询
     *
     * @param skillSub 筛选条件
     * @return 查询结果
     */
    @Override
    public List<SkillReq> query(SkillReq skill) {
        return this.skillSubDao.query(skill);
    }

    /**
     * 新增数据
     *
     * @param skillSub 实例对象
     * @return 实例对象
     */
    @Override
    public SkillSub insert(SkillSub skillSub) {
        Integer id = this.skillSubDao.maxId()+1;
        skillSub.setSkillId(id);
        this.skillSubDao.insert(skillSub);
        return skillSub;
    }

    /**
     * 修改数据
     *
     * @param skillSub 实例对象
     * @return 实例对象
     */
    @Override
    public SkillSub update(SkillSub skillSub) {
        this.skillSubDao.update(skillSub);
        return this.queryById(skillSub.getSkillId());
    }

    /**
     * 通过主键删除数据
     *
     * @param skillId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer skillId) {
        return this.skillSubDao.deleteById(skillId) > 0;
    }
}
