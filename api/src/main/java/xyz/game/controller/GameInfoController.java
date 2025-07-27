package xyz.game.controller;

import xyz.game.controller.global.DataWithPage;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.GameInfo;
import xyz.game.service.GameInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * (GameInfo)表控制层
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@RestController
@RequestMapping("gameInfo")
public class GameInfoController {
    /**
     * 服务对象
     */
    @Resource
    private GameInfoService gameInfoService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<ResponseData<DataWithPage<GameInfo>>> query() {
        ResponseData<DataWithPage<GameInfo>> resp = new ResponseData<>();
        DataWithPage<GameInfo> data = new DataWithPage<>();
        List<GameInfo> query = this.gameInfoService.query();
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
    public ResponseEntity<GameInfo> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.gameInfoService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param gameInfo 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<GameInfo> add(GameInfo gameInfo) {
        return ResponseEntity.ok(this.gameInfoService.insert(gameInfo));
    }

    /**
     * 编辑数据
     *
     * @param gameInfo 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<GameInfo> edit(GameInfo gameInfo) {
        return ResponseEntity.ok(this.gameInfoService.update(gameInfo));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.gameInfoService.deleteById(id));
    }

}

