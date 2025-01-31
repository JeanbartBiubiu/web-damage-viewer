<script lang="ts" setup>
import { ref, defineProps, defineEmits } from "vue"

interface ConditionGroup {
  variable: string
  operator: string
  value: string
  logic?: string
}

interface ActionGroup {
  property: string
  expression: string
  value: string
}

interface ConditionItem {
  conditions: ConditionGroup[]
  actions: ActionGroup[]
}

const props = defineProps<{
  modelValue: ConditionItem[]
}>()

const emit = defineEmits(["update:modelValue"])

// 变量选项
const variableOptions = [
  { label: "变量A", value: "A" },
  { label: "变量B", value: "B" },
  { label: "变量C", value: "C" }
]

// 条件选项
const operatorOptions = [
  { label: "等于", value: "=" },
  { label: "大于", value: ">" },
  { label: "小于", value: "<" }
]

// 逻辑选项
const logicOptions = [
  { label: "并且", value: "AND" },
  { label: "或者", value: "OR" }
]

// 属性选项
const propertyOptions = [
  { label: "属性1", value: "prop1" },
  { label: "属性2", value: "prop2" },
  { label: "属性3", value: "prop3" }
]

// 表达式选项
const expressionOptions = [
  { label: "表达式1", value: "expr1" },
  { label: "表达式2", value: "expr2" },
  { label: "表达式3", value: "expr3" }
]

const addItem = () => {
  const newList = [...props.modelValue]
  newList.push({
    conditions: [
      {
        variable: "",
        operator: "",
        value: "",
        logic: "AND"
      }
    ],
    actions: [
      {
        property: "",
        expression: "",
        value: ""
      }
    ]
  })
  emit("update:modelValue", newList)
}

const addCondition = (itemIndex: number) => {
  const newList = [...props.modelValue]
  newList[itemIndex].conditions.push({
    variable: "",
    operator: "",
    value: "",
    logic: "AND"
  })
  emit("update:modelValue", newList)
}

const addAction = (itemIndex: number) => {
  const newList = [...props.modelValue]
  newList[itemIndex].actions.push({
    property: "",
    expression: "",
    value: ""
  })
  emit("update:modelValue", newList)
}

const removeItem = (index: number) => {
  const newList = [...props.modelValue]
  newList.splice(index, 1)
  emit("update:modelValue", newList)
}

const removeCondition = (itemIndex: number, conditionIndex: number) => {
  const newList = [...props.modelValue]
  newList[itemIndex].conditions.splice(conditionIndex, 1)
  emit("update:modelValue", newList)
}

const removeAction = (itemIndex: number, actionIndex: number) => {
  const newList = [...props.modelValue]
  newList[itemIndex].actions.splice(actionIndex, 1)
  emit("update:modelValue", newList)
}

const updateCondition = (itemIndex: number, conditionIndex: number, key: string, value: string) => {
  const newList = [...props.modelValue]
  newList[itemIndex].conditions[conditionIndex][key] = value
  emit("update:modelValue", newList)
}

const updateAction = (itemIndex: number, actionIndex: number, key: string, value: string) => {
  const newList = [...props.modelValue]
  newList[itemIndex].actions[actionIndex][key] = value
  emit("update:modelValue", newList)
}
</script>

<template>
  <div class="condition-editor">
    <div v-for="(item, itemIndex) in modelValue" :key="itemIndex" class="condition-item">
      <!-- 条件部分 -->
      <div class="conditions-wrapper">
        <div v-for="(condition, conditionIndex) in item.conditions" :key="conditionIndex" class="condition-group">
          <el-select
            v-model="condition.variable"
            placeholder="选择变量"
            @change="(val) => updateCondition(itemIndex, conditionIndex, 'variable', val)"
          >
            <el-option
              v-for="option in variableOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>

          <el-select
            v-model="condition.operator"
            placeholder="选择条件"
            @change="(val) => updateCondition(itemIndex, conditionIndex, 'operator', val)"
          >
            <el-option
              v-for="option in operatorOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>

          <el-input
            v-model="condition.value"
            placeholder="输入判断值"
            @input="(val) => updateCondition(itemIndex, conditionIndex, 'value', val)"
          />

          <el-select
            v-if="conditionIndex < item.conditions.length - 1"
            v-model="condition.logic"
            placeholder="逻辑连接"
            @change="(val) => updateCondition(itemIndex, conditionIndex, 'logic', val)"
          >
            <el-option v-for="option in logicOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>

          <el-button
            v-if="item.conditions.length > 1"
            type="danger"
            circle
            icon="Delete"
            @click="removeCondition(itemIndex, conditionIndex)"
          />
        </div>
        <el-button type="primary" icon="Plus" @click="addCondition(itemIndex)">添加条件</el-button>
      </div>

      <!-- 动作部分 -->
      <div class="actions-wrapper">
        <div v-for="(action, actionIndex) in item.actions" :key="actionIndex" class="action-group">
          <el-select
            v-model="action.property"
            placeholder="选择属性"
            @change="(val) => updateAction(itemIndex, actionIndex, 'property', val)"
          >
            <el-option
              v-for="option in propertyOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>

          <el-select
            v-model="action.expression"
            placeholder="选择表达式"
            @change="(val) => updateAction(itemIndex, actionIndex, 'expression', val)"
          >
            <el-option
              v-for="option in expressionOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>

          <el-input
            v-model="action.value"
            placeholder="输入值"
            @input="(val) => updateAction(itemIndex, actionIndex, 'value', val)"
          />

          <el-button
            v-if="item.actions.length > 1"
            type="danger"
            circle
            icon="Delete"
            @click="removeAction(itemIndex, actionIndex)"
          />
        </div>
        <el-button type="primary" icon="Plus" @click="addAction(itemIndex)">添加动作</el-button>
      </div>

      <!-- 整组删除按钮 -->
      <el-button type="danger" circle icon="Delete" @click="removeItem(itemIndex)" />
    </div>

    <!-- 添加新组按钮 -->
    <el-button type="primary" icon="Plus" @click="addItem">添加条件组</el-button>
  </div>
</template>

<style lang="scss" scoped>
.condition-editor {
  .condition-item {
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    padding: 15px;
    margin-bottom: 15px;

    .conditions-wrapper,
    .actions-wrapper {
      margin-bottom: 15px;

      .condition-group,
      .action-group {
        display: flex;
        gap: 10px;
        margin-bottom: 10px;
        align-items: center;
      }

      :deep(.el-select),
      :deep(.el-input) {
        width: 150px;
      }
    }
  }
}
</style>
