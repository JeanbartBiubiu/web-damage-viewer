package xyz.game.service;

import xyz.game.entity.Attribute;
import xyz.game.entity.custom.AttributeReq;

import java.util.List;

/**
 * 属性表(Attribute)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface AttributeService {

    /**
     * 通过ID查询单条数据
     *
     * @param attributeId 主键
     * @return 实例对象
     */
    AttributeReq queryById(Integer attributeId);

    /**
     * 分页查询
     *
     * @param attribute 筛选条件
     * @return 查询结果
     */
    List<AttributeReq> query(AttributeReq attribute);

    /**
     * 新增数据
     *
     * @param attribute 实例对象
     * @return 实例对象
     */
    AttributeReq insert(AttributeReq attribute);

    /**
     * 修改数据
     *
     * @param attribute 实例对象
     * @return 实例对象
     */
    AttributeReq update(AttributeReq attribute);

    /**
     * 通过主键删除数据
     *
     * @param attributeId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer attributeId);

}
