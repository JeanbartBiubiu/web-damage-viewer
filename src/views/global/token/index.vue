<template>
  <div class="app-container">
    <el-card>
      <el-form>
        <el-form-item label="Token">
          <el-input v-model="token" placeholder="请输入Token" :show-password="true" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存/更新</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import { ElMessage } from "element-plus"

const token = ref("")

const handleSave = () => {
  if (token.value) {
    localStorage.setItem("token", token.value)
    ElMessage.success("保存token成功")
  } else {
    ElMessage.warning("请输入Token")
  }
}
</script>
