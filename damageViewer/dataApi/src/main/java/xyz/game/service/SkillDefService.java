package xyz.game.service;

import xyz.game.entity.SkillDef;

import java.util.List;

/**
 * 技能定义表(SkillDef)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface SkillDefService {

    /**
     * 通过ID查询单条数据
     *
     * @param skillId 主键
     * @return 实例对象
     */
    SkillDef queryById(Integer skillId);

    /**
     * 分页查询
     *
     * @param skillDef 筛选条件
     * @return 查询结果
     */
    List<SkillDef> query(SkillDef skillDef);

    /**
     * 新增数据
     *
     * @param skillDef 实例对象
     * @return 实例对象
     */
    SkillDef insert(SkillDef skillDef);

    /**
     * 修改数据
     *
     * @param skillDef 实例对象
     * @return 实例对象
     */
    SkillDef update(SkillDef skillDef);

    /**
     * 通过主键删除数据
     *
     * @param skillId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer skillId);

}
