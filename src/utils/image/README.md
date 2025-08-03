# 图片管理工具

本工具提供了在前端管理图片的完整解决方案，包括图片的上传、存储、同步和获取功能。

## 主要功能

1. **图片上传组件**：提供用户友好的图片上传界面
2. **本地存储**：使用IndexDB在浏览器本地存储图片数据
3. **服务器同步**：与服务器API进行图片数据同步
4. **图片管理**：提供图片的批量插入和获取功能

## 文件结构

- `image_manager.ts`：IndexDB图片管理核心类
- `sync-images.ts`：图片同步工具，用于从服务器同步图片到本地
- `README.md`：使用说明文档

## 使用方法

### 1. 图片上传组件

```vue
<template>
  <UploadImg64
    game="game1"
    type="character"
    :id="1"
    v-model:img="characterImage"
  />
</template>

<script setup>
import { ref } from 'vue'
import UploadImg64 from '@/components/UploadImg64/index.vue'

const characterImage = ref('')
</script>
```

#### 组件属性

- `game`：游戏标识，必填
- `type`：图片类型，必填
- `id`：图片ID，必填
- `img`：图片数据，可选，支持v-model绑定

### 2. 图片管理器API

```typescript
import { imageManager } from '@/utils/image/image_manager'

// 获取图片
const image = await imageManager.getImageByUri('game1_character_1')

// 获取所有图片
const allImages = await imageManager.getAllImages()

// 批量插入图片
const images = [
  {
    uri: 'game1_character_1',
    image: 'base64图片数据',
    create_time: new Date().toISOString(),
    update_time: new Date().toISOString()
  }
]
const count = await imageManager.batchInsert(images)
```

### 3. 图片同步工具

```typescript
import { syncImagesFromServer, syncImagesByUris } from '@/utils/image/sync-images'

// 同步所有图片
const count = await syncImagesFromServer()
console.log(`成功同步 ${count} 张图片`)

// 同步指定URI的图片
const uris = ['game1_character_1', 'game1_equipment_101']
const syncCount = await syncImagesByUris(uris)
```

## 图片URI规则

图片的唯一标识URI按照以下格式生成：

```
{game}_{type}_{id}
```

例如：`game1_character_1`、`game1_equipment_101`

## 数据结构

### IndexDB存储结构

```typescript
interface ImageData {
  uri: string            // 图片唯一标识
  create_time: string    // 创建时间
  update_time: string    // 更新时间
  image: string          // Base64图片数据
}
```

### API接口结构

```typescript
interface Image {
  uri: string                    // 图片唯一标识
  createTime: string | undefined // 创建时间
  updateTime: string | undefined // 更新时间
  image: string                  // Base64图片数据
}
```

## 注意事项

1. 确保图片大小适中，避免存储过大的图片数据
2. IndexDB存储空间有限，请合理管理图片数量
3. 图片上传组件会自动将图片裁剪为64x64像素的正方形
4. 组件会自动尝试从IndexDB加载图片，如果加载失败则显示空白