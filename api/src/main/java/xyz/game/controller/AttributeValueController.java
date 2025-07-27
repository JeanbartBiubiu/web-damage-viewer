package xyz.game.controller;

import org.apache.ibatis.annotations.Param;
import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.AttributeValue;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.AttributeValueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 单位-属性-等级对应数据数值(AttributeValue)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("useless")
public class AttributeValueController {
    /**
     * 服务对象
     */
    @Resource
    private AttributeValueService attributeValueService;

    /**
     * 分页查询
     *
     * @param attributeValue 筛选条件
     * @return 查询结果
     */
    @GetMapping()
    public ResponseEntity<List<AttributeValue>> query(AttributeValue attributeValue) {
        return ResponseEntity.ok(this.attributeValueService.query(attributeValue));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/value")
    public ResponseEntity<ResponseData<DataWithPage<AttributeValue>>> queryById(@Param("indivId") int id) {
        ResponseData<DataWithPage<AttributeValue>> resp = new ResponseData<>();
        DataWithPage<AttributeValue> data = new DataWithPage<>();
        data.setList(this.attributeValueService.queryById(id));
        data.setTotal(data.getList().size());
        resp.setData(data);
        return ResponseEntity.ok(resp);
    }

    /**
     * 新增数据
     *
     * @param attributeValue 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<AttributeValue> add(AttributeValue attributeValue) {
        return ResponseEntity.ok(this.attributeValueService.insert(attributeValue));
    }

    /**
     * 编辑数据
     *
     * @param attributeValue 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<AttributeValue> edit(AttributeValue attributeValue) {
        return ResponseEntity.ok(this.attributeValueService.update(attributeValue));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById( ) {
        return ResponseEntity.ok(this.attributeValueService.deleteById());
    }

}

