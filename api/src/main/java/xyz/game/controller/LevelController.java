package xyz.game.controller;

import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.Level;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 等级定义表(Level)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("level")
public class LevelController {
    /**
     * 服务对象
     */
    @Resource
    private LevelService levelService;

    /**
     * 分页查询
     *
     * @param level 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<Level>>>  query(Level level) {
        ResponseData<DataWithPage<Level>> resp = new ResponseData<>();
        DataWithPage<Level> data = new DataWithPage<>();
        data.setList(this.levelService.query(level));
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
    public ResponseEntity<Level> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.levelService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param level 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<ResponseData<Level>> add(@RequestBody Level level) {
        ResponseData<Level> resp = new ResponseData<>();
        resp.setData(this.levelService.insert(level));
        return ResponseEntity.ok(resp);
    }

    /**
     * 编辑数据
     *
     * @param level 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<ResponseData<Level>> edit(@RequestBody Level level) {
        ResponseData<Level> resp = new ResponseData<>();
        resp.setData(this.levelService.update(level));
        return ResponseEntity.ok(resp);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Boolean>> deleteById(@PathVariable("id") Integer id) {
        ResponseData<Boolean> resp = new ResponseData<>();
        resp.setData(this.levelService.deleteById(id));
        return ResponseEntity.ok(resp);
    }

}

