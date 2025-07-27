package xyz.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.game.dao.EquipIdDao;
import xyz.game.dao.EquipmentNameDao;
import xyz.game.entity.EquipId;
import xyz.game.entity.Equipment;
import xyz.game.dao.EquipmentDao;
import xyz.game.entity.EquipmentName;
import xyz.game.entity.custom.EquipmentReq;
import xyz.game.service.EquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 装备表(buff等状态效果也定义为装备)(Equipment)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("equipmentService")
public class EquipmentServiceImpl implements EquipmentService {
    @Resource
    private EquipmentDao equipmentDao;
    @Resource
    private EquipmentNameDao equipmentNameDao;
    @Resource
    private EquipIdDao equipIdDao;

    /**
     * 通过ID查询单条数据
     *
     * @param equipId 主键
     * @return 实例对象
     */
    @Override
    public EquipmentReq queryById(Integer equipId) {
        Equipment equipment = this.equipmentDao.queryById(equipId);
        EquipmentReq equipmentReq1 = new EquipmentReq();
        BeanUtils.copyProperties(equipment, equipmentReq1);
        List<EquipId> equipIds = this.equipIdDao.selectList(new QueryWrapper<EquipId>().eq("equip_id", equipId));
        equipmentReq1.setEquipIds(equipIds);
        return equipmentReq1;
    }

    /**
     * 分页查询
     *
     * @param equipment 筛选条件
     * @return 查询结果
     */
    @Override
    public List<EquipmentReq> query(EquipmentReq equipmentReq) {
        Equipment req = new Equipment();
        BeanUtils.copyProperties(equipmentReq, req);
        List<Equipment> query = this.equipmentDao.query(req);
        List<EquipmentReq> list = new ArrayList<>();
        for (Equipment equipment : query) {
            EquipmentReq equipmentReq1 = new EquipmentReq();
            BeanUtils.copyProperties(equipment, equipmentReq1);
            list.add(equipmentReq1);
        }
        return list;
    }

    /**
     * 新增数据
     *
     * @param equipment 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public EquipmentReq insert(EquipmentReq equipment) {
        Integer i = this.equipmentDao.maxId();
        if (i == null) {
            i = 0;
        }
        Integer id = i + 1;

        Equipment equipment1 = new Equipment();
        BeanUtils.copyProperties(equipment, equipment1);
        equipment1.setEquipmentId(id);
        this.equipmentDao.insert(equipment1);
        EquipmentName equipmentName = new EquipmentName();
        BeanUtils.copyProperties(equipment, equipmentName);
        equipmentName.setEquipmentId(id);
        equipmentName.setLanguageId(1);
        this.equipmentNameDao.insert(equipmentName);
        if (equipment.getEquipIds() != null) {
            equipment.getEquipIds().forEach(equipId -> equipId.setEquipId(id));
            this.equipIdDao.insert(equipment.getEquipIds());
        }
        return this.queryById(id);
    }

    /**
     * 修改数据
     *
     * @param equipment 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public EquipmentReq update(EquipmentReq equipment) {
        Equipment equipment1 = new Equipment();
        BeanUtils.copyProperties(equipment, equipment1);
        this.equipmentDao.update(equipment1);
        EquipmentName equipmentName = new EquipmentName();
        BeanUtils.copyProperties(equipment, equipmentName);
        this.equipmentNameDao.update(equipmentName);
        this.equipIdDao.delete(new QueryWrapper<EquipId>().eq("equip_id", equipment.getEquipmentId()));
        this.equipIdDao.insert(equipment.getEquipIds());
        return this.queryById(equipment.getEquipmentId());
    }

    /**
     * 通过主键删除数据
     *
     * @param equipId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer equipId) {
        this.equipIdDao.delete(new QueryWrapper<EquipId>().eq("equip_id", equipId));
        this.equipmentNameDao.deleteById(equipId);
        return this.equipmentDao.deleteById(equipId) > 0;
    }
}
