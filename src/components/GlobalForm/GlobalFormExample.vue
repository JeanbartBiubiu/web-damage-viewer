<script lang="ts" setup>
import { ref } from "vue"
import GlobalForm, { type FormField } from "./index.vue"
import { type GlobalFormData } from "@/utils/cache/local-storage"
import { ElMessage } from "element-plus"
import { Setting } from "@element-plus/icons-vue"

// 控制表单显示
const formVisible = ref(false)

// 定义表单字段配置
const formFields: FormField[] = [
  {
    key: 'apiBaseUrl',
    label: 'API基础地址',
    type: 'input',
    placeholder: '请输入API基础地址',
    required: true,
    defaultValue: 'http://localhost:8888'
  },
  {
    key: 'timeout',
    label: '请求超时时间',
    type: 'number',
    placeholder: '请输入超时时间(秒)',
    defaultValue: 30,
    rules: [
      { type: 'number', min: 1, max: 300, message: '超时时间必须在1-300秒之间' }
    ]
  },
  {
    key: 'environment',
    label: '运行环境',
    type: 'select',
    required: true,
    defaultValue: 'development',
    options: [
      { label: '开发环境', value: 'development' },
      { label: '测试环境', value: 'staging' },
      { label: '生产环境', value: 'production' }
    ]
  },
  {
    key: 'enableDebug',
    label: '启用调试模式',
    type: 'switch',
    defaultValue: true
  },
  {
    key: 'description',
    label: '配置说明',
    type: 'textarea',
    placeholder: '请输入配置说明',
    defaultValue: '这是全局配置表单的示例'
  },
  {
    key: 'lastUpdated',
    label: '最后更新时间',
    type: 'date',
    defaultValue: new Date().toISOString().split('T')[0]
  }
]

// 处理数据变化
const handleDataChange = (data: GlobalFormData) => {
  console.log('全局表单数据已更新:', data)
  ElMessage.success('配置数据已更新')
}

// 处理字段变化
const handleFieldChange = (key: string, value: any) => {
  console.log(`字段 ${key} 已更新为:`, value)
}

// 打开配置面板
const openSettings = () => {
  formVisible.value = true
}
</script>

<template>
  <div class="global-form-example">
    <!-- 触发按钮 -->
    <div class="settings-trigger">
      <el-tooltip content="全局配置" placement="left">
        <el-button 
          type="primary" 
          :icon="Setting" 
          circle 
          @click="openSettings"
          class="settings-btn"
        />
      </el-tooltip>
    </div>
    
    <!-- 全局表单组件 -->
    <GlobalForm
      v-model:visible="formVisible"
      :fields="formFields"
      title="全局配置面板"
      width="500px"
      :auto-save="true"
      :auto-save-delay="1000"
      @data-change="handleDataChange"
      @field-change="handleFieldChange"
    />
  </div>
</template>

<style lang="scss" scoped>
.global-form-example {
  .settings-trigger {
    position: fixed;
    top: 50%;
    right: 20px;
    transform: translateY(-50%);
    z-index: 1000;
    
    .settings-btn {
      width: 50px;
      height: 50px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      transition: all 0.3s ease;
      
      &:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
      }
    }
  }
}
</style>