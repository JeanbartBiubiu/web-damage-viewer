package xyz.game.controller;

import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.ComputingLifeCycle;
import xyz.game.entity.Equipment;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.entity.custom.EquipmentReq;
import xyz.game.service.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 装备表(buff等状态效果也定义为装备)(Equipment)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("equipment")
public class EquipmentController {
    /**
     * 服务对象
     */
    @Resource
    private EquipmentService equipmentService;

    /**
     * 分页查询
     *
     * @param equipment 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<EquipmentReq>>> query(EquipmentReq equipment) {
        ResponseData<DataWithPage<EquipmentReq>> resp = new ResponseData<>();
        DataWithPage<EquipmentReq> data = new DataWithPage<>();
        data.setList(this.equipmentService.query(equipment));
        data.setTotal(data.getList().size());
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<ResponseData<EquipmentReq>> queryById(@PathVariable("id") Integer id) {
        ResponseData<EquipmentReq> resp = new ResponseData<>();
        resp.setData(this.equipmentService.queryById(id));
        return ResponseEntity.ok(resp);
    }

    /**
     * 新增数据
     *
     * @param equipment 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<ResponseData<EquipmentReq>> add(@RequestBody EquipmentReq equipment) {
        ResponseData<EquipmentReq> resp = new ResponseData<>();
        resp.setData(this.equipmentService.insert(equipment));
        return ResponseEntity.ok(resp);
    }

    /**
     * 编辑数据
     *
     * @param equipment 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<ResponseData<EquipmentReq>> edit(@RequestBody EquipmentReq equipment) {
        ResponseData<EquipmentReq> resp = new ResponseData<>();
        resp.setData(this.equipmentService.update(equipment));
        return ResponseEntity.ok(resp);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.equipmentService.deleteById(id));
    }

}

