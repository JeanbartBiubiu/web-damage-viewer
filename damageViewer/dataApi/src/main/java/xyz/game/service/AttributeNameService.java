package xyz.game.service;

import xyz.game.entity.AttributeName;

import java.util.List;

/**
 * 属性名称表(AttributeName)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface AttributeNameService {

    /**
     * 通过ID查询单条数据
     *
     * @param attributeId 主键
     * @return 实例对象
     */
    AttributeName queryById(Integer attributeId);

    /**
     * 分页查询
     *
     * @param attributeName 筛选条件
     * @return 查询结果
     */
    List<AttributeName> query(AttributeName attributeName);

    /**
     * 新增数据
     *
     * @param attributeName 实例对象
     * @return 实例对象
     */
    AttributeName insert(AttributeName attributeName);

    /**
     * 修改数据
     *
     * @param attributeName 实例对象
     * @return 实例对象
     */
    AttributeName update(AttributeName attributeName);

    /**
     * 通过主键删除数据
     *
     * @param attributeId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer attributeId);

}
