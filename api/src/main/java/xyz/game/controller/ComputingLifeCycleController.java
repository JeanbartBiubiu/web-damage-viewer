package xyz.game.controller;

import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.ComputingLifeCycle;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.ComputingLifeCycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * (ComputingLifeCycle)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("lifeCycle")
public class ComputingLifeCycleController {
    /**
     * 服务对象
     */
    @Resource
    private ComputingLifeCycleService computingLifeCycleService;

    /**
     * 分页查询
     *
     * @param computingLifeCycle 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<ComputingLifeCycle>>> query(ComputingLifeCycle computingLifeCycle) {
        ResponseData<DataWithPage<ComputingLifeCycle>> resp = new ResponseData<>();
        DataWithPage<ComputingLifeCycle> data = new DataWithPage<>();
        data.setList(this.computingLifeCycleService.query(computingLifeCycle));
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
    public ResponseEntity<ComputingLifeCycle> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.computingLifeCycleService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param computingLifeCycle 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<ResponseData<ComputingLifeCycle>> add(@RequestBody ComputingLifeCycle computingLifeCycle) {
        ResponseData<ComputingLifeCycle> resp = new ResponseData<>();
        resp.setData(this.computingLifeCycleService.insert(computingLifeCycle));
        return ResponseEntity.ok(resp);
    }

    /**
     * 编辑数据
     *
     * @param computingLifeCycle 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<ResponseData<ComputingLifeCycle>> edit(@RequestBody ComputingLifeCycle computingLifeCycle) {
        ResponseData<ComputingLifeCycle> resp = new ResponseData<>();
        resp.setData(this.computingLifeCycleService.update(computingLifeCycle));
        return ResponseEntity.ok(resp);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Boolean>> deleteById(@PathVariable("id")Integer id) {
        ResponseData<Boolean> resp = new ResponseData<>();
        resp.setData(this.computingLifeCycleService.deleteById(id));
        return ResponseEntity.ok(resp);
    }

}

