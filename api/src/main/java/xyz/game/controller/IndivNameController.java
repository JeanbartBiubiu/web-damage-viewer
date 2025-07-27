package xyz.game.controller;

import org.apache.ibatis.annotations.Param;
import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.AttributeValue;
import xyz.game.entity.IndivName;
import xyz.game.entity.Individual;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.AttributeValueService;
import xyz.game.service.IndivNameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * (IndivName)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("indiv")
public class IndivNameController {
    /**
     * 服务对象
     */
    @Resource
    private IndivNameService indivNameService;

    @Resource
    private AttributeValueService attributeValueService;

    /**
     * 分页查询
     *
     * @param indivName 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<IndivName>>> query(IndivName indivName) {
        ResponseData<DataWithPage<IndivName>> resp = new ResponseData<>();
        DataWithPage<IndivName> data = new DataWithPage<>();
        data.setList(this.indivNameService.query(indivName));
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
    public ResponseEntity<IndivName> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.indivNameService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param indivName 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<ResponseData<IndivName>> add(@RequestBody IndivName indivName) {
        ResponseData<IndivName> resp = new ResponseData<>();
        resp.setData(this.indivNameService.insert(indivName));
        return ResponseEntity.ok(resp);
    }

    /**
     * 编辑数据
     *
     * @param indivName 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<ResponseData<IndivName>> edit(@RequestBody IndivName indivName) {
        ResponseData<IndivName> resp = new ResponseData<>();
        resp.setData(this.indivNameService.update(indivName));
        return ResponseEntity.ok(resp);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Boolean>> deleteById(@PathVariable(value = "id") Integer id) {
        ResponseData<Boolean> resp = new ResponseData<>();
        resp.setData(this.indivNameService.deleteById(id));
        return ResponseEntity.ok(resp);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/values")
    public ResponseEntity<ResponseData<DataWithPage<AttributeValue>>> queryById(@RequestParam("indivId") int id) {
        ResponseData<DataWithPage<AttributeValue>> resp = new ResponseData<>();
        DataWithPage<AttributeValue> data = new DataWithPage<>();
        data.setList(this.attributeValueService.queryById(id));
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
    @PutMapping("/values")
    public ResponseEntity<ResponseData<Boolean>> settingValues(@RequestBody List<AttributeValue> list) {
        ResponseData<Boolean> resp = new ResponseData<>();
        resp.setData(this.attributeValueService.settingValues(list));
        return ResponseEntity.ok(resp);
    }

}

