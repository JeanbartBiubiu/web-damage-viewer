package xyz.game.service.impl;

import xyz.game.dao.DDLDao;
import xyz.game.entity.GameInfo;
import xyz.game.dao.GameInfoDao;
import xyz.game.service.GameInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (GameInfo)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("gameInfoService")
public class GameInfoServiceImpl implements GameInfoService {
    private final GameInfoDao gameInfoDao;
    private final DDLDao ddlDao;

    public GameInfoServiceImpl(GameInfoDao gameInfoDao,DDLDao ddlDao) {
        this.gameInfoDao = gameInfoDao;
        this.ddlDao = ddlDao;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param gameId 主键
     * @return 实例对象
     */
    @Override
    public GameInfo queryById(Integer gameId) {
        return this.gameInfoDao.queryById(gameId);
    }

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @Override
    public List<GameInfo> query() {
        return this.gameInfoDao.query();
    }

    /**
     * 新增数据
     *
     * @param gameInfo 实例对象
     * @return 实例对象
     */
    @Override
    public GameInfo insert(GameInfo gameInfo) {
        // todo DDL语句执行算事务？
        this.gameInfoDao.insert(gameInfo);
        ddlDao.createDb(gameInfo.getGameCode());
        ddlDao.createTableAttribute(gameInfo.getGameCode());
        ddlDao.createTableAttributeName(gameInfo.getGameCode());
        ddlDao.createTableAttributeValue(gameInfo.getGameCode());
        ddlDao.createTableComputingLifeCycle(gameInfo.getGameCode());
        ddlDao.createTableEquipId(gameInfo.getGameCode());
        ddlDao.createTableEquipment(gameInfo.getGameCode());
        ddlDao.createTableEquipmentName(gameInfo.getGameCode());
        ddlDao.createTableFormulaDefault(gameInfo.getGameCode());
        ddlDao.createTableFormulaImpl(gameInfo.getGameCode());
        ddlDao.createTableIndividual(gameInfo.getGameCode());
        ddlDao.createTableIndivName(gameInfo.getGameCode());
        ddlDao.createTableIndivSkill(gameInfo.getGameCode());
        ddlDao.createTableLanguage(gameInfo.getGameCode());
        ddlDao.createTableLevel(gameInfo.getGameCode());
        ddlDao.createTableSkillDef(gameInfo.getGameCode());
        ddlDao.createTableSkillSub(gameInfo.getGameCode());
        ddlDao.createTableVersion(gameInfo.getGameCode());
        return gameInfo;
    }

    /**
     * 修改数据
     *
     * @param gameInfo 实例对象
     * @return 实例对象
     */
    @Override
    public GameInfo update(GameInfo gameInfo) {
        this.gameInfoDao.update(gameInfo);
        return this.queryById(gameInfo.getGameId());
    }

    /**
     * 通过主键删除数据
     *
     * @param gameId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer gameId) {
        return this.gameInfoDao.deleteById(gameId) > 0;
    }
}
