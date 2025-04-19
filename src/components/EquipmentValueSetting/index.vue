<template>
  <div class="attribute-editor">
    <el-form label-width="120px">
      <el-form-item v-for="attr in sortedAttributes" :key="attr.attributeId" :label="attr.attributeName">
        <div class="input-group">
          <el-input-number
            v-model="attr.addValue"
            :precision="0"
            :step="1"
            @wheel.prevent="handleWheel($event, attr, 'addValue')"
            placeholder="加成值"
          />
          <el-input-number
            v-model="attr.multiValue"
            :precision="2"
            :step="0.1"
            :default-value="0"
            @wheel.prevent="handleWheel($event, attr, 'multiValue')"
            placeholder="倍率"
          />
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue"
import { getAttribute } from "@/api/attribute"

const props = defineProps<{
  equipId?: number
  editData?: Array<{
    attributeId: number
    addValue: number
    multiValue: number
  }>
}>()

const values = ref<
  Array<{
    equipId: number
    attributeId: number
    attributeName: string
    addValue: number
    multiValue: number
  }>
>([])

const emits = defineEmits(["update:values"])

// 监听 values 的变化，触发事件
watch(
  values,
  (newValues) => {
    emits("update:values", newValues)
  },
  { deep: true }
)

// 监听 editData 变化
watch(
  () => props.editData,
  (newEditData) => {
    if (newEditData) {
      newEditData.forEach((item) => {
        const attr = values.value.find((a) => a.attributeId === item.attributeId)
        if (attr) {
          attr.equipId = props.equipId || 0
          attr.addValue = item.addValue
          attr.multiValue = item.multiValue
        }
      })
    }
  },
  { deep: true }
)

// 监听 equipId 变化
watch(
  () => props.equipId,
  (newEquipId) => {
    values.value.forEach((attr) => {
      attr.equipId = newEquipId || 0
    })
  }
)

// 根据是否有值排序属性
const sortedAttributes = computed(() => {
  return [...values.value].sort((a, b) => {
    const aHasValue = props.editData?.some((item) => item.attributeId === a.attributeId)
    const bHasValue = props.editData?.some((item) => item.attributeId === b.attributeId)
    if (aHasValue && !bHasValue) return -1
    if (!aHasValue && bHasValue) return 1
    return 0
  })
})

// 处理滚轮事件
const handleWheel = (event: WheelEvent, attr: (typeof values.value)[0], field: "addValue" | "multiValue") => {
  const step = field === "addValue" ? 1 : 0.1
  const direction = event.deltaY > 0 ? -1 : 1
  const currentValue = attr[field] || 0
  attr[field] = Number((currentValue + direction * step).toFixed(2))
}

// 获取属性列表
const fetchAttributes = async () => {
  try {
    const res = await getAttribute({})
    values.value = res.data.list.map((attr) => ({
      equipId: props.equipId || 0,
      attributeId: attr.attributeId,
      attributeName: attr.attributeName,
      addValue: 0,
      multiValue: 0
    }))
    console.log(props.editData)

    // 如果是编辑模式，设置已有值
    if (props.editData) {
      props.editData.forEach((item) => {
        const attr = values.value.find((a) => a.attributeId === item.attributeId)
        if (attr) {
          attr.equipId = props.equipId || 0
          attr.addValue = item.addValue
          attr.multiValue = item.multiValue
        }
      })
    }
  } catch (error) {
    console.error("获取属性列表失败:", error)
  }
}

// 获取属性值
const getAttributeValues = () => {
  return values.value
    .filter((attr) => attr.addValue !== 0 || attr.multiValue !== 0)
    .map((attr) => ({
      attributeId: attr.attributeId,
      addValue: attr.addValue,
      multiValue: attr.multiValue
    }))
}

// 暴露方法给父组件
defineExpose({
  getAttributeValues
})

onMounted(() => {
  fetchAttributes()
})
</script>

<style scoped>
.attribute-editor {
  padding: 20px;
}

.input-group {
  display: flex;
  gap: 20px;
}

:deep(.el-input-number) {
  width: 180px;
}
</style>
