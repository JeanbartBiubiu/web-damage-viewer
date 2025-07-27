package xyz.game.controller;

import xyz.game.entity.AttributeName;
import xyz.game.service.AttributeNameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 属性名称表(AttributeName)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("attributeName")
public class AttributeNameController {
    /**
     * 服务对象
     */
    @Resource
    private AttributeNameService attributeNameService;

    /**
     * 分页查询
     *
     * @param attributeName 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<List<AttributeName>> query(AttributeName attributeName) {
        return ResponseEntity.ok(this.attributeNameService.query(attributeName));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<AttributeName> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.attributeNameService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param attributeName 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<AttributeName> add(AttributeName attributeName) {
        return ResponseEntity.ok(this.attributeNameService.insert(attributeName));
    }

    /**
     * 编辑数据
     *
     * @param attributeName 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<AttributeName> edit(AttributeName attributeName) {
        return ResponseEntity.ok(this.attributeNameService.update(attributeName));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.attributeNameService.deleteById(id));
    }

}

