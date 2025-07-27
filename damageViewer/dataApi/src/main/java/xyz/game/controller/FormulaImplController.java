package xyz.game.controller;

import xyz.game.entity.FormulaImpl;
import xyz.game.service.FormulaImplService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * (FormulaImpl)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("formulaImpl")
public class FormulaImplController {
    /**
     * 服务对象
     */
    @Resource
    private FormulaImplService formulaImplService;

    /**
     * 分页查询
     *
     * @param formulaImpl 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<List<FormulaImpl>> query(FormulaImpl formulaImpl) {
        return ResponseEntity.ok(this.formulaImplService.query(formulaImpl));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<FormulaImpl> queryById(@PathVariable("id")  int id) {
        return ResponseEntity.ok(this.formulaImplService.queryById());
    }

    /**
     * 新增数据
     *
     * @param formulaImpl 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<FormulaImpl> add(FormulaImpl formulaImpl) {
        return ResponseEntity.ok(this.formulaImplService.insert(formulaImpl));
    }

    /**
     * 编辑数据
     *
     * @param formulaImpl 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<FormulaImpl> edit(FormulaImpl formulaImpl) {
        return ResponseEntity.ok(this.formulaImplService.update(formulaImpl));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(int  id) {
        return ResponseEntity.ok(this.formulaImplService.deleteById());
    }

}

