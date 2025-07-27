package xyz.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.game.controller.global.ResponseData;
import xyz.game.entity.Type;
import xyz.game.entity.TypeDTO;
import xyz.game.service.TypeService;

import java.util.List;

/**
 * 类型管理控制器
 */
@RestController
@RequestMapping("/types")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 查询类型列表
     */
    @GetMapping
    public ResponseEntity<ResponseData<List<Type>>> listTypes(
            @RequestParam(required = false) String keyword) {
        List<Type> result = typeService.Types(keyword);
        ResponseData<List<Type>> responseData = new ResponseData<>();
        responseData.setData(result);
        return ResponseEntity.ok(responseData);
    }

    // ... existing code ...
    /**
     * 获取类型详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Type>> getType(@PathVariable Long id) {
        Type type = typeService.getTypeById(id);
        ResponseData<Type> responseData = new ResponseData<>();
        responseData.setData(type);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 创建类型及其关系
     */
    @PostMapping
    public ResponseEntity<ResponseData<Boolean>> createType(@RequestBody TypeDTO typeDTO) {
        boolean result = typeService.createTypeWithRelations(typeDTO);
        ResponseData<Boolean> responseData = new ResponseData<>();
        responseData.setData(result);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 更新类型及其关系
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Boolean>> updateType(@PathVariable Long id, @RequestBody TypeDTO typeDTO) {
        typeDTO.getType().setId(id);
        boolean result = typeService.updateTypeWithRelations(typeDTO);
        ResponseData<Boolean> responseData = new ResponseData<>();
        responseData.setData(result);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 删除类型
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Boolean>> deleteType(@PathVariable Long id) {
        boolean result = typeService.deleteType(id);
        ResponseData<Boolean> responseData = new ResponseData<>();
        responseData.setData(result);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 获取类型的父类型列表
     */
    @GetMapping("/{id}/parents")
    public ResponseEntity<ResponseData<List<Type>>> getParentTypes(@PathVariable Long id) {
        List<Type> parents = typeService.getParentTypes(id);
        ResponseData<List<Type>> responseData = new ResponseData<>();
        responseData.setData(parents);
        return ResponseEntity.ok(responseData);
    }

    /**
     * 获取类型的子类型列表
     */
    @GetMapping("/{id}/children")
    public ResponseEntity<ResponseData<List<Type>>> getChildTypes(@PathVariable Long id) {
        List<Type> children = typeService.getChildTypes(id);
        ResponseData<List<Type>> responseData = new ResponseData<>();
        responseData.setData(children);
        return ResponseEntity.ok(responseData);
    }
} 