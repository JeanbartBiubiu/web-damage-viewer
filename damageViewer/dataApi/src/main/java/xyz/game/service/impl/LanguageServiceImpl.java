package xyz.game.service.impl;

import xyz.game.entity.Language;
import xyz.game.dao.LanguageDao;
import xyz.game.service.LanguageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * (Language)表服务实现类
 *
 * @author makejava
 * @since 2024-06-23 21:23:34
 */
@Service("languageService")
public class LanguageServiceImpl implements LanguageService {
    @Resource
    private LanguageDao languageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param languageId 主键
     * @return 实例对象
     */
    @Override
    public Language queryById(Integer languageId) {
        return this.languageDao.queryById(languageId);
    }

    /**
     * 分页查询
     *
     * @param language 筛选条件
     * @return 查询结果
     */
    @Override
    public List<Language> query(Language language) {
        return this.languageDao.query(language);
    }

    /**
     * 新增数据
     *
     * @param language 实例对象
     * @return 实例对象
     */
    @Override
    public Language insert(Language language) {
        this.languageDao.insert(language);
        return language;
    }

    /**
     * 修改数据
     *
     * @param language 实例对象
     * @return 实例对象
     */
    @Override
    public Language update(Language language) {
        this.languageDao.update(language);
        return this.queryById(language.getLanguageId());
    }

    /**
     * 通过主键删除数据
     *
     * @param languageId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer languageId) {
        return this.languageDao.deleteById(languageId) > 0;
    }
}
