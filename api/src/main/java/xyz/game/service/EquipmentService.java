package xyz.game.service;

import xyz.game.entity.Equipment;
import xyz.game.entity.custom.EquipmentReq;

import java.util.List;

/**
 * 装备表(buff等状态效果也定义为装备)(Equipment)表服务接口
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
public interface EquipmentService {

    /**
     * 通过ID查询单条数据
     *
     * @param equipId 主键
     * @return 实例对象
     */
    EquipmentReq queryById(Integer equipId);

    /**
     * 分页查询
     *
     * @param equipment 筛选条件
     * @return 查询结果
     */
    List<EquipmentReq> query(EquipmentReq equipment);

    /**
     * 新增数据
     *
     * @param equipment 实例对象
     * @return 实例对象
     */
    EquipmentReq insert(EquipmentReq equipment);

    /**
     * 修改数据
     *
     * @param equipment 实例对象
     * @return 实例对象
     */
    EquipmentReq update(EquipmentReq equipment);

    /**
     * 通过主键删除数据
     *
     * @param equipId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer equipId);

}
