<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { createLifeCycle, deleteLifeCycle, updateLifeCycle, getLifeCycle } from "@/api/lifeCycle"
import { type LifeCycleRequestData, type GetLifeCycleData } from "@/api/lifeCycle/types/lifeCycle"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { cloneDeep } from "lodash-es"

defineOptions({
  // 命名当前组件
  name: "LifeCycleMge"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: LifeCycleRequestData = {
  cycleId: undefined,
  cycleType: "",
  cycleCode: "",
  cycleName: "",
}
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formData = ref<LifeCycleRequestData>(cloneDeep(DEFAULT_FORM_DATA))
const formRules: FormRules<LifeCycleRequestData> = {
  cycleOrder: [{ required: true, trigger: "blur", message: "请输入排序值" }],
  cycleType: [{ required: true, trigger: "blur", message: "请设置类型" }],
  cycleCode: [{ required: true, trigger: "blur", message: "请输入code值" }],
  cycleName: [{ required: true, trigger: "blur", message: "请输入名称" }],
}
const handleCreateOrUpdate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    loading.value = true
    const api = formData.value.cycleId === undefined ? createLifeCycle : updateLifeCycle
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
const resetForm = () => {
  formRef.value?.clearValidate()
  formData.value = cloneDeep(DEFAULT_FORM_DATA)
}
//#endregion

//#region 删
const handleDelete = (row: GetLifeCycleData) => {
  ElMessageBox.confirm(`正在删除生命周期：${row.cycleName}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteLifeCycle(row.cycleId).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const handleUpdate = (row: GetLifeCycleData) => {
  dialogVisible.value = true
  formData.value = cloneDeep(row)
}
//#endregion

//#region 查
const tableData = ref<GetLifeCycleData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  cycleId: undefined,
  cycleType: "",
  cycleCode: "",
  cycleName: "",
})
const getTableData = () => {
  loading.value = true
  getLifeCycle({
    // currentPage: paginationData.currentPage,
    // size: paginationData.pageSize,
    cycleId: searchData.cycleId || undefined,
    cycleType: searchData.cycleType || "",
    cycleCode: searchData.cycleCode || "",
    cycleName: searchData.cycleName || "",
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
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="cycleId" label="周期id">
          <el-input v-model="searchData.cycleId" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="cycleType" label="类型">
          <el-select v-model="searchData.cycleType" width="100px">
              <el-option label="之前" value="before"/>
              <el-option label="当" value="when"/>
              <el-option label="之后" value="after"/>
              <el-option label="其他" value="other"/>
            </el-select>
        </el-form-item>
        <el-form-item prop="cycleCode" label="代码">
          <el-input v-model="searchData.cycleCode" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="cycleName" label="名称">
          <el-input v-model="searchData.cycleName" placeholder="请输入" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-loading="loading" shadow="never">
      <div class="toolbar-wrapper">
        <div>
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增生命周期</el-button>
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
          <el-table-column prop="cycleId" label="周期id" align="center" />
          <el-table-column prop="cycleOrder" label="排序值" align="center"/>
          <el-table-column prop="cycleType" label="类型" align="center" :formatter="function"/>
          <el-table-column prop="cycleCode" label="代码" align="center" />
          <el-table-column prop="cycleName" label="名称" align="center" />
          <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
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
      :title="formData.cycleId === undefined ? '新增周期' : '修改周期'"
      @closed="resetForm"
      width="30%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item prop="cycleOrder" label="排序值">
          <el-input v-model="formData.cycleOrder" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="cycleType" label="类型">
            <el-select v-model="formData.cycleType">
              <el-option label="之前" value="before"/>
              <el-option label="当" value="when"/>
              <el-option label="之后" value="after"/>
              <el-option label="其他" value="other"/>
            </el-select>
        </el-form-item>
        <el-form-item prop="cycleCode" label="代码">
          <el-input v-model="formData.cycleCode" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="cycleName" label="名称">
          <el-input v-model="formData.cycleName" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateOrUpdate" :loading="loading">确认</el-button>
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
