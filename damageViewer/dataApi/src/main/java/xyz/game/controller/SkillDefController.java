package xyz.game.controller;

import xyz.game.entity.SkillDef;
import xyz.game.service.SkillDefService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 技能定义表(SkillDef)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("skillDef")
public class SkillDefController {
    /**
     * 服务对象
     */
    @Resource
    private SkillDefService skillDefService;

    /**
     * 分页查询
     *
     * @param skillDef 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<List<SkillDef>> query(SkillDef skillDef) {
        return ResponseEntity.ok(this.skillDefService.query(skillDef));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<SkillDef> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.skillDefService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param skillDef 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<SkillDef> add(SkillDef skillDef) {
        return ResponseEntity.ok(this.skillDefService.insert(skillDef));
    }

    /**
     * 编辑数据
     *
     * @param skillDef 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<SkillDef> edit(SkillDef skillDef) {
        return ResponseEntity.ok(this.skillDefService.update(skillDef));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.skillDefService.deleteById(id));
    }

}

