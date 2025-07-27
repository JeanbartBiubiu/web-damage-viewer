package xyz.game.service;

import xyz.game.entity.SkillSub;
import xyz.game.entity.custom.SkillReq;

import java.util.List;

/**
 * (SkillSub)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface SkillSubService {

    /**
     * 通过ID查询单条数据
     *
     * @param skillId 主键
     * @return 实例对象
     */
    SkillSub queryById(Integer skillId);

    /**
     * 分页查询
     *
     * @param skillSub 筛选条件
     * @return 查询结果
     */
    List<SkillReq> query(SkillReq skill);

    /**
     * 新增数据
     *
     * @param skillSub 实例对象
     * @return 实例对象
     */
    SkillSub insert(SkillSub skillSub);

    /**
     * 修改数据
     *
     * @param skillSub 实例对象
     * @return 实例对象
     */
    SkillSub update(SkillSub skillSub);

    /**
     * 通过主键删除数据
     *
     * @param skillId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer skillId);

}
