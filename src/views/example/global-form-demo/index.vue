<script lang="ts" setup>
import { ref, computed } from "vue"
import { useGlobalForm, useTypedGlobalForm, type GlobalFormFields } from "@/hooks/useGlobalForm"
import { ElMessage } from "element-plus"
import { Refresh, Setting, Delete } from "@element-plus/icons-vue"

defineOptions({
  name: "GlobalFormDemo"
})

// 使用全局表单Hook
const { 
  globalFormData, 
  getField, 
  setField, 
  clearData, 
  refreshData,
  getApiConfig,
  isDebugMode,
  getCurrentEnvironment
} = useGlobalForm()

// 使用带类型的Hook
const typedHook = useTypedGlobalForm<GlobalFormFields>()

// 本地状态
const loading = ref(false)
const showRawData = ref(false)

// 计算属性
const apiConfig = computed(() => getApiConfig())
const debugMode = computed(() => isDebugMode())
const environment = computed(() => getCurrentEnvironment())

// 格式化的数据显示
const formattedData = computed(() => {
  return JSON.stringify(globalFormData, null, 2)
})

// 测试API配置
const testApiConfig = async () => {
  loading.value = true
  try {
    const config = getApiConfig()
    console.log('当前API配置:', config)
    
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(`API测试成功！环境: ${config.environment}, 超时: ${config.timeout}ms`)
  } catch (error) {
    ElMessage.error('API测试失败')
  } finally {
    loading.value = false
  }
}

// 更新单个字段
const updateField = (key: string, value: any) => {
  setField(key, value)
  ElMessage.success(`字段 ${key} 已更新`)
}

// 批量更新示例数据
const updateExampleData = () => {
  const exampleData = {
    apiBaseUrl: 'https://api.example.com',
    timeout: 60,
    environment: 'production' as const,
    enableDebug: false,
    description: '这是批量更新的示例数据',
    lastUpdated: new Date().toISOString().split('T')[0]
  }
  
  Object.entries(exampleData).forEach(([key, value]) => {
    setField(key, value)
  })
  
  ElMessage.success('示例数据已更新')
}

// 清除所有数据
const handleClearData = () => {
  clearData()
  ElMessage.success('所有数据已清除')
}

// 刷新数据
const handleRefreshData = () => {
  refreshData()
  ElMessage.success('数据已刷新')
}
</script>

<template>
  <div class="app-container">
    <el-card shadow="never" class="demo-card">
      <template #header>
        <div class="card-header">
          <span>全局表单数据演示</span>
          <div class="header-buttons">
            <el-button :icon="Refresh" @click="handleRefreshData">刷新数据</el-button>
            <el-button type="danger" :icon="Delete" @click="handleClearData">清除数据</el-button>
          </div>
        </div>
      </template>
      
      <!-- 当前配置概览 -->
      <div class="config-overview">
        <h3>当前配置概览</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="API地址" :value="getField('apiBaseUrl', '未设置')" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="超时时间" :value="getField('timeout', 0)" suffix="秒" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="运行环境" :value="environment" />
          </el-col>
          <el-col :span="6">
            <div class="debug-status">
              <span>调试模式:</span>
              <el-tag :type="debugMode ? 'success' : 'info'">
                {{ debugMode ? '开启' : '关闭' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- API配置测试 -->
      <div class="api-test-section">
        <h3>API配置测试</h3>
        <el-card>
          <p>当前API配置:</p>
          <pre>{{ JSON.stringify(apiConfig, null, 2) }}</pre>
          <el-button 
            type="primary" 
            @click="testApiConfig" 
            :loading="loading"
            style="margin-top: 10px"
          >
            测试API配置
          </el-button>
        </el-card>
      </div>
      
      <!-- 快速操作 -->
      <div class="quick-actions">
        <h3>快速操作</h3>
        <el-space wrap>
          <el-button @click="updateField('apiBaseUrl', 'http://localhost:3000')">
            设置本地API
          </el-button>
          <el-button @click="updateField('environment', 'development')">
            切换到开发环境
          </el-button>
          <el-button @click="updateField('enableDebug', !debugMode)">
            {{ debugMode ? '关闭' : '开启' }}调试模式
          </el-button>
          <el-button @click="updateExampleData">
            加载示例数据
          </el-button>
        </el-space>
      </div>
      
      <!-- 数据展示 -->
      <div class="data-display">
        <h3>
          原始数据
          <el-switch 
            v-model="showRawData" 
            style="margin-left: 10px"
            active-text="显示"
            inactive-text="隐藏"
          />
        </h3>
        <el-collapse-transition>
          <el-card v-show="showRawData">
            <pre>{{ formattedData }}</pre>
          </el-card>
        </el-collapse-transition>
      </div>
      
      <!-- 使用说明 -->
      <div class="usage-guide">
        <h3>使用说明</h3>
        <el-alert 
          title="如何使用全局表单数据" 
          type="info" 
          :closable="false"
        >
          <template #default>
            <ol>
              <li>点击右侧的设置按钮打开全局配置面板</li>
              <li>在配置面板中编辑字段，数据会自动保存到localStorage</li>
              <li>在任何组件中使用 <code>useGlobalForm()</code> Hook来访问数据</li>
              <li>数据会在页面刷新后保持不变</li>
              <li>可以通过API接口读取这些配置用于前端逻辑</li>
            </ol>
          </template>
        </el-alert>
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.demo-card {
  margin-bottom: 20px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-buttons {
      display: flex;
      gap: 10px;
    }
  }
}

.config-overview {
  margin-bottom: 30px;
  
  h3 {
    margin-bottom: 15px;
    color: var(--el-text-color-primary);
  }
  
  .debug-status {
    display: flex;
    align-items: center;
    gap: 8px;
    height: 32px;
  }
}

.api-test-section {
  margin-bottom: 30px;
  
  h3 {
    margin-bottom: 15px;
    color: var(--el-text-color-primary);
  }
  
  pre {
    background-color: var(--el-bg-color-page);
    padding: 10px;
    border-radius: 4px;
    font-size: 12px;
    margin: 0;
  }
}

.quick-actions {
  margin-bottom: 30px;
  
  h3 {
    margin-bottom: 15px;
    color: var(--el-text-color-primary);
  }
}

.data-display {
  margin-bottom: 30px;
  
  h3 {
    margin-bottom: 15px;
    color: var(--el-text-color-primary);
    display: flex;
    align-items: center;
  }
  
  pre {
    background-color: var(--el-bg-color-page);
    padding: 15px;
    border-radius: 4px;
    font-size: 12px;
    margin: 0;
    max-height: 300px;
    overflow-y: auto;
  }
}

.usage-guide {
  h3 {
    margin-bottom: 15px;
    color: var(--el-text-color-primary);
  }
  
  :deep(.el-alert__content) {
    ol {
      margin: 0;
      padding-left: 20px;
      
      li {
        margin-bottom: 8px;
        line-height: 1.5;
        
        code {
          background-color: var(--el-bg-color-page);
          padding: 2px 6px;
          border-radius: 3px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>