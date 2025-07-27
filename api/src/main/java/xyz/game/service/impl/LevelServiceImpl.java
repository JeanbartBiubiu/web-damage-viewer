package xyz.game.service.impl;

import xyz.game.entity.Level;
import xyz.game.dao.LevelDao;
import xyz.game.service.LevelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * 等级定义表(Level)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("levelService")
public class LevelServiceImpl implements LevelService {
    @Resource
    private LevelDao levelDao;

    /**
     * 通过ID查询单条数据
     *
     * @param levelId 主键
     * @return 实例对象
     */
    @Override
    public Level queryById(Integer levelId) {
        return this.levelDao.queryById(levelId);
    }

    /**
     * 分页查询
     *
     * @param level 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Level> query(Level level) {
        return this.levelDao.query(level);
    }

    /**
     * 新增数据
     *
     * @param level 实例对象
     * @return 实例对象
     */
    @Override
    public Level insert(Level level) {
        Integer id = this.levelDao.maxId();
        if (id == null){
            id = 1;
        }else{
            id +=1 ;
        }
        level.setLevelId(id);
        this.levelDao.insert(level);
        return level;
    }

    /**
     * 修改数据
     *
     * @param level 实例对象
     * @return 实例对象
     */
    @Override
    public Level update(Level level) {
        this.levelDao.update(level);
        return this.queryById(level.getLevelId());
    }

    /**
     * 通过主键删除数据
     *
     * @param levelId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer levelId) {
        return this.levelDao.deleteById(levelId) > 0;
    }
}
