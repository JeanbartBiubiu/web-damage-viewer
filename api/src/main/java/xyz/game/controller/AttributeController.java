package xyz.game.controller;

import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.game.service.IndividualService;

import java.util.List;

/**
 * 属性表(Attribute)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("/attribute")
public class AttributeController {
    /**
     * 服务对象
     */
    private final AttributeService attributeService;

    private final IndividualService individualService;

    public AttributeController(AttributeService attributeService, IndividualService individualService) {
        this.attributeService = attributeService;
        this.individualService = individualService;
    }

    /**
     * 分页查询
     *
     * @param attribute 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<AttributeReq>>> query(AttributeReq attribute) {
        ResponseData<DataWithPage<AttributeReq>> resp = new ResponseData<>();
        DataWithPage<AttributeReq> data = new DataWithPage<>();
        List<AttributeReq> query = this.attributeService.query(attribute);
        data.setList(query);
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
    public ResponseEntity<AttributeReq> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.attributeService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param attribute 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<ResponseData<AttributeReq>> add(@RequestBody AttributeReq attribute) {
        ResponseData<AttributeReq> resp = new ResponseData<>();
        resp.setData(this.attributeService.insert(attribute));
        return ResponseEntity.ok(resp);
    }

    /**
     * 编辑数据
     *
     * @param attribute 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<ResponseData<AttributeReq>> edit(@RequestBody AttributeReq attribute) {
        ResponseData<AttributeReq> resp = new ResponseData<>();
        resp.setData(this.attributeService.update(attribute));
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
        resp.setData(this.attributeService.deleteById(id));
        return ResponseEntity.ok(resp);
    }

}

