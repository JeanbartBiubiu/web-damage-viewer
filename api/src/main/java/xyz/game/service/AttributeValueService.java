package xyz.game.service;

import xyz.game.entity.AttributeValue;

import java.util.List;

/**
 * 单位-属性-等级对应数据数值(AttributeValue)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface AttributeValueService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    List<AttributeValue> queryById(int indivId);

    boolean settingValues(List<AttributeValue> list);

    /**
     * 分页查询
     *
     * @param attributeValue 筛选条件
     * @return 查询结果
     */
    List<AttributeValue> query(AttributeValue attributeValue);

    /**
     * 新增数据
     *
     * @param attributeValue 实例对象
     * @return 实例对象
     */
    AttributeValue insert(AttributeValue attributeValue);

    /**
     * 修改数据
     *
     * @param attributeValue 实例对象
     * @return 实例对象
     */
    AttributeValue update(AttributeValue attributeValue);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById( );

}
