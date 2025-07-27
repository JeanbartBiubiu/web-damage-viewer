package xyz.game.service;

import xyz.game.entity.IndivSkill;

import java.util.List;

/**
 * 人物-技能关联表(IndivSkill)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface IndivSkillService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    IndivSkill queryById( );

    /**
     * 分页查询
     *
     * @param indivSkill 筛选条件
     * @return 查询结果
     */
    List<IndivSkill> query(IndivSkill indivSkill);

    /**
     * 新增数据
     *
     * @param indivSkill 实例对象
     * @return 实例对象
     */
    IndivSkill insert(IndivSkill indivSkill);

    /**
     * 修改数据
     *
     * @param indivSkill 实例对象
     * @return 实例对象
     */
    IndivSkill update(IndivSkill indivSkill);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById( );

}
