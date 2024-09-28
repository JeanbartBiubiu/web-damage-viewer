<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { getLevel } from "@/api/level"
import { getAttribute } from "@/api/attribute"
import { createIndiv, updateIndiv, deleteIndiv, getIndiv, getIndivdualValues, settingIndivAttrValues } from "@/api/individual"
import { type GetAttributeData } from "@/api/attribute/types/attribute"
import { type GetLevelData } from "@/api/level/types/level"
import {
  type IndivRequestData,
  type IndivAttributeValueRequestData,
  type GetIndivAttributeValueData,
  type GetIndivData,
  type IndivAttributeValueHolisticData,
  settingHolisticValue
} from "@/api/individual/types/individual"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { cloneDeep, update } from "lodash-es"

defineOptions({
  // 命名当前组件
  name: "IndivMge"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: IndivRequestData = {
  indivId: undefined,
  indivName: ""
}
const dialogVisible = ref<boolean>(false)
const settingAttribute = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formData = ref<IndivRequestData>(cloneDeep(DEFAULT_FORM_DATA))
const valueFormData = ref<Array<IndivAttributeValueRequestData>>()
const formRules: FormRules<IndivRequestData> = {
  indivName: [{ required: true, trigger: "blur", message: "请输入角色名" }]
}

const handleCreateOrUpdate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    loading.value = true
    const api = formData.value.indivId === undefined ? createIndiv : updateIndiv
    api(formData.value)
      .then(() => {
        ElMessage.success("操作成功")
        dialogVisible.value = false
        getTableData()
      })
      .finally(() => {
        loading.value = false
      })
  })
}

const settingValues = () => {
  let tempArray = ref<Array<GetIndivAttributeValueData>>()
  tempArray.value = new Array<GetIndivAttributeValueData>()
  // 将list转换为后端要的结构
  settingTableListValue.value.forEach(item => {
      for (let index = 1; index <= 50; index++) {
        if (item['value'+index] == undefined) {
          break;
        }
        var tempObject = {
            indivId: indivId.value,
            attributeId: item.attributeId,
            levelId: index,
            value: item['value'+index]
        }
        tempArray.value.push(tempObject)
      }
  });
    settingIndivAttrValues(tempArray.value)
      .then(() => {
        ElMessage.success("操作成功")
        settingAttribute.value = false
        getTableData()
      })
      .finally(() => {
        loading.value = false
      })
}
const resetForm = () => {
  formRef.value?.clearValidate()
  formData.value = cloneDeep(DEFAULT_FORM_DATA)
}
//#endregion

//#region 删
const handleDelete = (row: GetIndivData) => {
  ElMessageBox.confirm(`正在删除属角色：${row.indivName}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteIndiv(row.indivId).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const handleUpdate = (row: GetIndivData) => {
  dialogVisible.value = true
  formData.value = cloneDeep(row)
}
//#endregion

// 设置属性值
const handleSettingAttribute = (row: GetIndivData) => {
  getAttributeData(row)
  settingAttribute.value = true
}

//#region 查
const tableData = ref<GetIndivData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  indivId: 0,
  indivName: ""
})
interface tableStruct {
  attributeName: string
  value1: number
  value2: number
}
const getTableData = () => {
  loading.value = true
  getIndiv({
    // currentPage: paginationData.currentPage,
    // size: paginationData.pageSize,
    indivId: searchData.indivId || undefined,
    indivName: searchData.indivName || ""
  })
    .then(({ data }) => {
      paginationData.total = data.total
      tableData.value = data.list
    })
    .catch(() => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}
const settingTableMapValue = ref<Map<number, IndivAttributeValueHolisticData>>()
const settingTableListValue = ref<IndivAttributeValueHolisticData[]>([])
const attributeValues = ref<GetIndivAttributeValueData[]>([])
// 属性定义
let baseAttrDef = ref<GetAttributeData[]>([])
// 等级定义
let baseLevelDef = ref<GetLevelData[]>([])
// 具体属性值map
let valueMap = ref<Map<number, Map<number, GetIndivAttributeValueData>>>()
let indivId = ref<number>()
const getAttributeData = (params: IndivRequestData) => {
  loading.value = true
  indivId.value = params.indivId
  getLevel({})
    .then(({ data }) => {
      while (baseLevelDef.value.length) {
        baseLevelDef.value.pop()
      }
      data.list.forEach((levelItem) => {
        baseLevelDef.value.push(levelItem)
      })
    })
    .catch()
    .finally()
  getAttribute({
    type: 1
  })
    .then(({ data }) => {
      while (baseAttrDef.value.length) {
        baseAttrDef.value.pop()
      }
      data.list.forEach((attrItem) => {
        baseAttrDef.value.push(attrItem)
      })
    })
    .catch()
    .finally()

  getIndivdualValues({
    indivId: params.indivId
  })
    .then(({ data }) => {
      settingTableMapValue.value = new Map<number, IndivAttributeValueHolisticData>()
      baseAttrDef.value.forEach((attrItem) => {
        settingTableMapValue.value?.set(attrItem.attributeId, {
          attributeId: attrItem.attributeId,
          attributeName: attrItem.attributeName
        })
      })
      // todo 库里没值后端也要给0
      data.list.forEach((element) => {
        valueMap.value = new Map<number, Map<number, GetIndivAttributeValueData>>()
        if (!valueMap.value?.has(element.levelId)) {
          valueMap.value?.set(element.levelId, new Map<number, GetIndivAttributeValueData>())
        }
        valueMap.value?.get(element.levelId)?.set(element.indivId, element)
        attributeValues.value.push(element)

        settingHolisticValue(element.levelId, element.value, settingTableMapValue.value?.get(element.attributeId)!)
      })
      while (settingTableListValue.value.length) {
        settingTableListValue.value.pop()
      }
      settingTableMapValue.value?.forEach((item) => {
        settingTableListValue.value.push({
          attributeId: item.attributeId,
          attributeName: item.attributeName,
          value1: item.value1,
          value2: item.value2,
          value3: item.value3,
          value4: item.value4,
          value5: item.value5,
          value6: item.value6,
          value7: item.value7,
          value8: item.value8,
          value9: item.value9,
          value10: item.value10,
          value11: item.value11,
          value12: item.value12,
          value13: item.value13,
          value14: item.value14,
          value15: item.value15,
          value16: item.value16,
          value17: item.value17,
          value18: item.value18,
          value19: item.value19,
          value20: item.value20,
          value21: item.value21,
          value22: item.value22,
          value23: item.value23,
          value24: item.value24,
          value25: item.value25,
          value26: item.value26,
          value27: item.value27,
          value28: item.value28,
          value29: item.value29,
          value30: item.value30,
          value31: item.value31,
          value32: item.value32,
          value33: item.value33,
          value34: item.value34,
          value35: item.value35,
          value36: item.value36,
          value37: item.value37,
          value38: item.value38,
          value39: item.value39,
          value40: item.value40,
          value41: item.value41,
          value42: item.value42,
          value43: item.value43,
          value44: item.value44,
          value45: item.value45,
          value46: item.value46,
          value47: item.value47,
          value48: item.value48,
          value49: item.value49,
          value50: item.value50
        })
      })
    })
    .catch(() => {
      console.log("有问题辣")
    })
    .finally(() => {})
  loading.value = false
}
const handleSearch = () => {
  paginationData.currentPage === 1 ? getTableData() : (paginationData.currentPage = 1)
}
const resetSearch = () => {
  searchFormRef.value?.resetFields()
  handleSearch()
}
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增角色</el-button>
          <el-button type="danger" :icon="Delete">批量删除</el-button>
        </div>
        <div>
          <el-tooltip content="下载">
            <el-button type="primary" :icon="Download" circle />
          </el-tooltip>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip>
        </div>
      </div>
      <div class="table-wrapper">
        <el-table :data="tableData">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column prop="indivImg" label="角色图片" align="center" />
          <el-table-column prop="indivId" label="角色id" align="center" />
          <el-table-column prop="indivName" label="角色名称" align="center" />
          <el-table-column fixed="right" label="操作" width="350" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="primary" text bg size="small" @click="handleSettingAttribute(scope.row)"
                >属性设置
              </el-button>
              <el-button type="danger" text bg size="small" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pager-wrapper">
        <el-pagination
          background
          :layout="paginationData.layout"
          :page-sizes="paginationData.pageSizes"
          :total="paginationData.total"
          :page-size="paginationData.pageSize"
          :currentPage="paginationData.currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    <!-- 新增/修改 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formData.indivId === undefined ? '新增角色' : '修改角色'"
      @closed="resetForm"
      width="30%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item prop="indivName" label="角色名">
          <el-input v-model="formData.indivName" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateOrUpdate" :loading="loading">确认</el-button>
      </template>
    </el-dialog>
    <!-- 设置属性值 -->
    <el-dialog v-model="settingAttribute" title="设置属性值" @closed="resetForm" width="50%">
      <el-form ref="formRef" :model="valueFormData" :rules="formRules" label-width="300px" label-position="left">
        <el-table :data="settingTableListValue" style="width: 100%">
          <el-table-column prop="attributeName" label="属性" :width="180">
          </el-table-column>
          <el-table-column
            v-for="(levelItem, index) in baseLevelDef"
            :prop="'value' + (index + 1)"
            :label="levelItem.levelName"
            :width="180"
          >
            <template v-slot="scope">
              <el-input type="text" v-model="scope.row['value' + (index + 1)]" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <el-button @click="settingAttribute = false">取消</el-button>
        <el-button type="primary" @click="settingValues" :loading="loading">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.search-wrapper {
  margin-bottom: 20px;

  :deep(.el-card__body) {
    padding-bottom: 2px;
  }
}

.toolbar-wrapper {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.table-wrapper {
  margin-bottom: 20px;
}

.pager-wrapper {
  display: flex;
  justify-content: flex-end;
}
</style>
