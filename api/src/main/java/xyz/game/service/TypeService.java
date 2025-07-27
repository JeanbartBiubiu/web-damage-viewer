package xyz.game.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.game.entity.Type;
import xyz.game.entity.TypeDTO;

import java.util.List;

/**
 * 类型服务接口
 */
public interface TypeService extends IService<Type> {
    
    /**
     * 查询类型列表
     * @param keyword 关键字搜索
     * @return 类型列表
     */
    List<Type> Types(String keyword);
    
    /**
     * 根据ID查询类型
     * @param id 类型ID
     * @return 类型
     */
    Type getTypeById(Long id);
    
    /**
     * 创建类型
     * @param type 类型信息
     * @return 是否成功
     */
    boolean createType(Type type);
    
    /**
     * 创建类型及其关系
     * @param typeDTO 类型及父类型信息
     * @return 是否成功
     */
    boolean createTypeWithRelations(TypeDTO typeDTO);
    
    /**
     * 更新类型
     * @param type 类型信息
     * @return 是否成功
     */
    boolean updateType(Type type);
    
    /**
     * 更新类型及其关系
     * @param typeDTO 类型及父类型信息
     * @return 是否成功
     */
    boolean updateTypeWithRelations(TypeDTO typeDTO);
    
    /**
     * 删除类型
     * @param id 类型ID
     * @return 是否成功
     */
    boolean deleteType(Long id);
    
    /**
     * 获取类型的所有父类型
     * @param typeId 类型ID
     * @return 父类型列表
     */
    List<Type> getParentTypes(Long typeId);
    
    /**
     * 获取类型的所有子类型
     * @param parentTypeId 父类型ID
     * @return 子类型列表
     */
    List<Type> getChildTypes(Long parentTypeId);
    
    /**
     * 添加类型关系
     * @param typeId 类型ID
     * @param parentTypeId 父类型ID
     * @return 是否成功
     */
    boolean addTypeRelation(Long typeId, Long parentTypeId);
    
    /**
     * 删除类型关系
     * @param typeId 类型ID
     * @param parentTypeId 父类型ID
     * @return 是否成功
     */
    boolean removeTypeRelation(Long typeId, Long parentTypeId);
} 