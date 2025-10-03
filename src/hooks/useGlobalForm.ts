import { ref, reactive, watch } from "vue"
import { 
  getGlobalFormData, 
  setGlobalFormData, 
  updateGlobalFormField,
  getGlobalFormField,
  clearGlobalFormData,
  type GlobalFormData 
} from "@/utils/cache/local-storage"

/**
 * 全局表单数据管理 Hook
 * 提供便捷的方法来管理和使用全局表单数据
 */
export function useGlobalForm() {
  // 响应式的全局表单数据
  const globalFormData = reactive<GlobalFormData>(getGlobalFormData())
  
  // 加载状态
  const loading = ref(false)
  
  // 刷新数据
  const refreshData = () => {
    const data = getGlobalFormData()
    Object.keys(globalFormData).forEach(key => {
      delete globalFormData[key]
    })
    Object.assign(globalFormData, data)
  }
  
  // 获取指定字段的值
  const getField = <T = any>(key: string, defaultValue?: T): T => {
    return getGlobalFormField(key, defaultValue)
  }
  
  // 设置指定字段的值
  const setField = (key: string, value: any) => {
    globalFormData[key] = value
    updateGlobalFormField(key, value)
  }
  
  // 批量设置数据
  const setData = (data: Partial<GlobalFormData>) => {
    Object.assign(globalFormData, data)
    setGlobalFormData(globalFormData)
  }
  
  // 清除所有数据
  const clearData = () => {
    Object.keys(globalFormData).forEach(key => {
      delete globalFormData[key]
    })
    clearGlobalFormData()
  }
  
  // 监听数据变化并同步到localStorage
  watch(
    () => globalFormData,
    (newData) => {
      setGlobalFormData(newData)
    },
    { deep: true }
  )
  
  // 获取API相关配置的便捷方法
  const getApiConfig = () => {
    return {
      baseUrl: getField('apiBaseUrl', 'http://localhost:8888'),
      timeout: getField('timeout', 30) * 1000, // 转换为毫秒
      environment: getField('environment', 'development'),
      enableDebug: getField('enableDebug', false)
    }
  }
  
  // 检查是否为调试模式
  const isDebugMode = () => {
    return getField('enableDebug', false)
  }
  
  // 获取当前环境
  const getCurrentEnvironment = () => {
    return getField('environment', 'development')
  }
  
  return {
    // 数据
    globalFormData,
    loading,
    
    // 方法
    refreshData,
    getField,
    setField,
    setData,
    clearData,
    
    // 便捷方法
    getApiConfig,
    isDebugMode,
    getCurrentEnvironment
  }
}

/**
 * 全局表单字段类型定义
 * 用于TypeScript类型提示
 */
export interface GlobalFormFields {
  apiBaseUrl?: string
  timeout?: number
  environment?: 'development' | 'staging' | 'production'
  enableDebug?: boolean
  description?: string
  lastUpdated?: string
  [key: string]: any
}

/**
 * 带类型的全局表单Hook
 */
export function useTypedGlobalForm<T extends GlobalFormFields = GlobalFormFields>() {
  const hook = useGlobalForm()
  
  return {
    ...hook,
    globalFormData: hook.globalFormData as T,
    getField: <K extends keyof T>(key: K, defaultValue?: T[K]): T[K] => {
      return hook.getField(key as string, defaultValue)
    },
    setField: <K extends keyof T>(key: K, value: T[K]) => {
      hook.setField(key as string, value)
    }
  }
}