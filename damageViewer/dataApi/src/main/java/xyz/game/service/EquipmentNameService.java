package xyz.game.service;

import xyz.game.entity.EquipmentName;

import java.util.List;

/**
 * (EquipmentName)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface EquipmentNameService {

    /**
     * 通过ID查询单条数据
     *
     * @param equipId 主键
     * @return 实例对象
     */
    EquipmentName queryById(Integer equipId);

    /**
     * 分页查询
     *
     * @param equipmentName 筛选条件
     * @return 查询结果
     */
    List<EquipmentName> query(EquipmentName equipmentName);

    /**
     * 新增数据
     *
     * @param equipmentName 实例对象
     * @return 实例对象
     */
    EquipmentName insert(EquipmentName equipmentName);

    /**
     * 修改数据
     *
     * @param equipmentName 实例对象
     * @return 实例对象
     */
    EquipmentName update(EquipmentName equipmentName);

    /**
     * 通过主键删除数据
     *
     * @param equipId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer equipId);

}
