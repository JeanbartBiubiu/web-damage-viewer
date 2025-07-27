package xyz.game.controller;

import xyz.game.entity.IndivSkill;
import xyz.game.service.IndivSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 人物-技能关联表(IndivSkill)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("indivSkill")
public class IndivSkillController {
    /**
     * 服务对象
     */
    @Resource
    private IndivSkillService indivSkillService;

    /**
     * 分页查询
     *
     * @param indivSkill 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<List<IndivSkill>> query(IndivSkill indivSkill) {
        return ResponseEntity.ok(this.indivSkillService.query(indivSkill));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<IndivSkill> queryById(@PathVariable("id")int   id) {
        return ResponseEntity.ok(this.indivSkillService.queryById());
    }

    /**
     * 新增数据
     *
     * @param indivSkill 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<IndivSkill> add(IndivSkill indivSkill) {
        return ResponseEntity.ok(this.indivSkillService.insert(indivSkill));
    }

    /**
     * 编辑数据
     *
     * @param indivSkill 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<IndivSkill> edit(IndivSkill indivSkill) {
        return ResponseEntity.ok(this.indivSkillService.update(indivSkill));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(int  id) {
        return ResponseEntity.ok(this.indivSkillService.deleteById());
    }

}

