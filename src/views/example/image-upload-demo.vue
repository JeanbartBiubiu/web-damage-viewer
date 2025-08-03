<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>图片上传组件示例</span>
          <el-button type="primary" @click="syncImages" :loading="syncing">从服务器同步图片</el-button>
        </div>
      </template>
      <div class="demo-container">
        <div class="upload-item">
          <h3>游戏角色头像</h3>
          <UploadImg64 game="game1" type="character" :id="1" v-model:img="characterImage" />
        </div>

        <div class="upload-item">
          <h3>游戏装备图标</h3>
          <UploadImg64 game="game1" type="equipment" :id="101" v-model:img="equipmentImage" />
        </div>

        <div class="upload-item">
          <h3>游戏技能图标</h3>
          <UploadImg64 game="game1" type="skill" :id="201" v-model:img="skillImage" />
        </div>
      </div>

      <el-divider content-position="center">使用说明</el-divider>

      <div class="description">
        <h4>组件功能</h4>
        <p>UploadImg64组件现在支持以下功能：</p>
        <ul>
          <li>自动从IndexDB加载已存在的图片</li>
          <li>上传并裁剪图片为64x64像素</li>
          <li>自动保存到服务器和本地IndexDB</li>
        </ul>

        <h4>参数说明</h4>
        <ul>
          <li><code>game</code>: 游戏标识，必填</li>
          <li><code>type</code>: 图片类型，必填</li>
          <li><code>id</code>: 图片ID，必填</li>
          <li><code>img</code>: 图片数据，可选，支持v-model绑定</li>
        </ul>

        <h4>URI生成规则</h4>
        <p>图片的唯一标识URI按照 <code>game_type_id</code> 格式生成，例如：<code>game1_character_1</code></p>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue"
import UploadImg64 from "@/components/UploadImg64/index.vue"
import { syncImagesFromServer } from "@/utils/image/sync-images"
import { ElMessage } from "element-plus"

// 图片数据绑定
const characterImage = ref<string>("")
const equipmentImage = ref<string>("")
const skillImage = ref<string>("")

// 同步状态
const syncing = ref(false)

// 从服务器同步图片到IndexDB
async function syncImages() {
  syncing.value = true
  try {
    const count = await syncImagesFromServer()
    if (count > 0) {
      ElMessage.success(`成功同步 ${count} 张图片到本地数据库`)
    } else {
      ElMessage.info("没有新图片需要同步")
    }
  } catch (error) {
    console.error("同步图片失败:", error)
    ElMessage.error("同步图片失败，请查看控制台获取详细信息")
  } finally {
    syncing.value = false
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-button {
  margin-left: 16px;
}

.demo-container {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

.upload-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.description {
  margin-top: 20px;
}

.description h4 {
  margin-top: 16px;
  margin-bottom: 8px;
}

.description ul {
  padding-left: 20px;
}

.description code {
  background-color: #f5f7fa;
  padding: 2px 4px;
  border-radius: 4px;
  color: #409eff;
}
</style>
