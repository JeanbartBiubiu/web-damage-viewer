<script lang="ts" setup>
import { reactive, ref, watch, onMounted, defineEmits, defineProps } from "vue"
import { type FormInstance, type FormRules, ElMessage } from "element-plus"
import { 
  getGlobalFormData, 
  setGlobalFormData, 
  updateGlobalFormField,
  type GlobalFormData 
} from "@/utils/cache/local-storage"

// 定义表单字段类型
export interface FormField {
  key: string
  label: string
  type: 'input' | 'textarea' | 'number' | 'select' | 'switch' | 'date'
  placeholder?: string
  required?: boolean
  options?: Array<{ label: string; value: any }>
  defaultValue?: any
  rules?: any[]
}

// 组件属性
interface Props {
  /** 表单字段配置 */
  fields: FormField[]
  /** 表单标题 */
  title?: string
  /** 是否显示 */
  visible?: boolean
  /** 是否自动保存 */
  autoSave?: boolean
  /** 自动保存延迟时间(ms) */
  autoSaveDelay?: number
  /** 表单宽度 */
  width?: string
  /** 是否可拖拽 */
  draggable?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '全局配置',
  visible: true,
  autoSave: true,
  autoSaveDelay: 500,
  width: '400px',
  draggable: true
})

// 事件定义
const emit = defineEmits<{
  'update:visible': [value: boolean]
  'data-change': [data: GlobalFormData]
  'field-change': [key: string, value: any]
}>()

// 响应式数据
const formRef = ref<FormInstance | null>(null)
const formData = reactive<GlobalFormData>({})
const loading = ref(false)
const dialogVisible = ref(props.visible)
const autoSaveTimer = ref<NodeJS.Timeout | null>(null)

// 动态生成表单规则
const formRules = ref<FormRules>({})

// 初始化表单数据和规则
const initForm = () => {
  const savedData = getGlobalFormData()
  const rules: FormRules = {}
  
  props.fields.forEach(field => {
    // 设置初始值
    formData[field.key] = savedData[field.key] !== undefined 
      ? savedData[field.key] 
      : field.defaultValue
    
    // 设置验证规则
    if (field.required || field.rules) {
      rules[field.key] = [
        ...(field.required ? [{ required: true, message: `请输入${field.label}`, trigger: 'blur' }] : []),
        ...(field.rules || [])
      ]
    }
  })
  
  formRules.value = rules
}

// 自动保存功能
const autoSaveData = () => {
  if (!props.autoSave) return
  
  if (autoSaveTimer.value) {
    clearTimeout(autoSaveTimer.value)
  }
  
  autoSaveTimer.value = setTimeout(() => {
    setGlobalFormData(formData)
    emit('data-change', { ...formData })
    ElMessage.success('配置已自动保存')
  }, props.autoSaveDelay)
}

// 手动保存
const handleSave = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    setGlobalFormData(formData)
    emit('data-change', { ...formData })
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('表单验证失败，请检查输入')
  } finally {
    loading.value = false
  }
}

// 重置表单
const handleReset = () => {
  formRef.value?.resetFields()
  props.fields.forEach(field => {
    formData[field.key] = field.defaultValue
  })
  setGlobalFormData(formData)
  ElMessage.success('已重置为默认值')
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  emit('update:visible', false)
}

// 监听表单数据变化
watch(
  () => formData,
  (newData) => {
    // 发出字段变化事件
    Object.keys(newData).forEach(key => {
      emit('field-change', key, newData[key])
    })
    
    // 自动保存
    autoSaveData()
  },
  { deep: true }
)

// 监听visible属性变化
watch(
  () => props.visible,
  (newVal) => {
    dialogVisible.value = newVal
  }
)

// 组件挂载时初始化
onMounted(() => {
  initForm()
})

// 暴露方法给父组件
defineExpose({
  save: handleSave,
  reset: handleReset,
  getFormData: () => ({ ...formData }),
  setFormData: (data: GlobalFormData) => {
    Object.assign(formData, data)
    setGlobalFormData(formData)
  }
})
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="title"
    :width="width"
    :draggable="draggable"
    @close="handleClose"
    class="global-form-dialog"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      label-position="left"
      v-loading="loading"
    >
      <template v-for="field in fields" :key="field.key">
        <!-- 输入框 -->
        <el-form-item 
          v-if="field.type === 'input'"
          :prop="field.key" 
          :label="field.label"
        >
          <el-input
            v-model="formData[field.key]"
            :placeholder="field.placeholder || `请输入${field.label}`"
            clearable
          />
        </el-form-item>
        
        <!-- 文本域 -->
        <el-form-item 
          v-else-if="field.type === 'textarea'"
          :prop="field.key" 
          :label="field.label"
        >
          <el-input
            v-model="formData[field.key]"
            type="textarea"
            :placeholder="field.placeholder || `请输入${field.label}`"
            :rows="3"
          />
        </el-form-item>
        
        <!-- 数字输入框 -->
        <el-form-item 
          v-else-if="field.type === 'number'"
          :prop="field.key" 
          :label="field.label"
        >
          <el-input-number
            v-model="formData[field.key]"
            :placeholder="field.placeholder || `请输入${field.label}`"
            style="width: 100%"
          />
        </el-form-item>
        
        <!-- 选择器 -->
        <el-form-item 
          v-else-if="field.type === 'select'"
          :prop="field.key" 
          :label="field.label"
        >
          <el-select
            v-model="formData[field.key]"
            :placeholder="field.placeholder || `请选择${field.label}`"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="option in field.options"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        
        <!-- 开关 -->
        <el-form-item 
          v-else-if="field.type === 'switch'"
          :prop="field.key" 
          :label="field.label"
        >
          <el-switch v-model="formData[field.key]" />
        </el-form-item>
        
        <!-- 日期选择器 -->
        <el-form-item 
          v-else-if="field.type === 'date'"
          :prop="field.key" 
          :label="field.label"
        >
          <el-date-picker
            v-model="formData[field.key]"
            type="date"
            :placeholder="field.placeholder || `请选择${field.label}`"
            style="width: 100%"
          />
        </el-form-item>
      </template>
    </el-form>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleReset">重置</el-button>
        <el-button @click="handleClose">关闭</el-button>
        <el-button type="primary" @click="handleSave" :loading="loading">
          保存
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.global-form-dialog {
  :deep(.el-dialog__header) {
    background-color: var(--el-color-primary-light-9);
    margin: 0;
    padding: 15px 20px;
    border-bottom: 1px solid var(--el-border-color-light);
  }
  
  :deep(.el-dialog__body) {
    padding: 20px;
  }
  
  :deep(.el-dialog__footer) {
    padding: 15px 20px;
    border-top: 1px solid var(--el-border-color-light);
    background-color: var(--el-bg-color-page);
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>