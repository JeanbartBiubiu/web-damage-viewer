package xyz.game.controller;

import xyz.game.controller.global.ResponseData;
import xyz.game.entity.Individual;
import xyz.game.entity.custom.AttributeReq;
import xyz.game.service.IndividualService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * (Individual)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("individual")
public class IndividualController {
    /**
     * 服务对象
     */
    @Resource
    private IndividualService individualService;

    /**
     * 分页查询
     *
     * @param individual 筛选条件
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<List<Individual>> query(Individual individual) {
        return ResponseEntity.ok(this.individualService.query(individual));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Individual> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.individualService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param individual 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<ResponseData<Individual>> add(Individual individual) {
        ResponseData<Individual> resp = new ResponseData<>();
        resp.setData(this.individualService.insert(individual));
        return ResponseEntity.ok(resp);
    }

    /**
     * 编辑数据
     *
     * @param individual 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<ResponseData<Individual>> edit(Individual individual) {
        ResponseData<Individual> resp = new ResponseData<>();
        resp.setData(this.individualService.update(individual));
        return ResponseEntity.ok(resp);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<ResponseData<Boolean>> deleteById(Integer id) {
        ResponseData<Boolean> resp = new ResponseData<>();
        resp.setData(this.individualService.deleteById(id));
        return ResponseEntity.ok(resp);
    }

}

