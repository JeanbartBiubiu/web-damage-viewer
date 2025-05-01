<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { ElMessage, ElMessageBox, FormInstance } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, RefreshRight } from "@element-plus/icons-vue"
import { Type, TypeDTO, getTypes, createType, updateType, deleteType, getParentTypes } from "@/api/type"

defineOptions({
  name: "TypeManagement"
})

//#region 表格相关
const loading = ref<boolean>(false)
const tableData = ref<Type[]>([])

//#region 分页相关
const paginationData = reactive({
  total: 0,
  currentPage: 1,
  pageSize: 10,
  pageSizes: [10, 20, 50],
  layout: "total, sizes, prev, pager, next, jumper"
})
const handleSizeChange = (val: number) => {
  paginationData.pageSize = val
}
const handleCurrentChange = (val: number) => {
  paginationData.currentPage = val
}
//#endregion

//#region 搜索相关
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  keyword: ""
})
const handleSearch = () => {
  paginationData.currentPage === 1 ? getTableData() : (paginationData.currentPage = 1)
}
const resetSearch = () => {
  searchFormRef.value?.resetFields()
  handleSearch()
}
//#endregion

//#region 获取表格数据
const getTableData = () => {
  loading.value = true
  getTypes(searchData.keyword || undefined)
    .then(({ data }) => {
      tableData.value = data
      paginationData.total = data.length
    })
    .catch(() => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}
//#endregion

//#region 表单相关
const dialogVisible = ref<boolean>(false)
const dialogTitle = ref<string>("新增类型")
const formRef = ref<FormInstance | null>(null)
const formData = reactive<TypeDTO>({
  type: {
    id: undefined,
    name: "",
    description: ""
  },
  parentTypeIds: []
})
const formRules = {
  "type.name": [{ required: true, message: "请输入类型名称", trigger: "blur" }]
}

// 重置表单
const resetForm = () => {
  formData.type = {
    id: undefined,
    name: "",
    description: ""
  }
  formData.parentTypeIds = []
}

// 选择父类型相关
const parentOptions = ref<Type[]>([])
const loadParentTypes = () => {
  getTypes()
    .then(({ data }) => {
      parentOptions.value = data
    })
    .catch(() => {
      parentOptions.value = []
    })
}

// 新增类型
const handleCreate = () => {
  loadParentTypes()
  dialogTitle.value = "新增类型"
  dialogVisible.value = true
  resetForm()
}

// 修改类型
const handleUpdate = (row: Type) => {
  loadParentTypes()
  dialogTitle.value = "修改类型"
  dialogVisible.value = true

  // 复制数据
  formData.type = JSON.parse(JSON.stringify(row))

  // 获取父类型
  if (row.id) {
    getParentTypes(row.id)
      .then(({ data }) => {
        formData.parentTypeIds = data.map((item) => item.id as number)
      })
      .catch(() => {
        formData.parentTypeIds = []
      })
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate((valid) => {
    if (valid) {
      const isCreate = formData.type.id === undefined
      const successMsg = isCreate ? "创建成功" : "修改成功"

      loading.value = true

      if (isCreate) {
        // 修正调用方式，确保参数正确
        createType(formData)
          .then(({ data }) => {
            if (data) {
              ElMessage.success(successMsg)
              dialogVisible.value = false
              getTableData()
            }
          })
          .catch(() => {
            ElMessage.error(isCreate ? "创建失败" : "修改失败")
          })
          .finally(() => {
            loading.value = false
          })
      } else {
        updateType(formData.type.id as number, formData)
          .then(({ data }) => {
            if (data) {
              ElMessage.success(successMsg)
              dialogVisible.value = false
              getTableData()
            }
          })
          .catch(() => {
            ElMessage.error(isCreate ? "创建失败" : "修改失败")
          })
          .finally(() => {
            loading.value = false
          })
      }
    }
  })
}
//#endregion

//#region 删除相关
const handleDelete = (row: Type) => {
  ElMessageBox.confirm(`确认删除类型 ${row.name} 吗？`, "警告", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      if (row.id !== undefined) {
        loading.value = true
        deleteType(row.id)
          .then(({ data }) => {
            if (data) {
              ElMessage.success("删除成功")
              getTableData()
            }
          })
          .catch(() => {
            ElMessage.error("删除失败")
          })
          .finally(() => {
            loading.value = false
          })
      }
    })
    .catch(() => {
      // 取消操作
    })
}
//#endregion

//#region 子类型相关
const childrenDialogVisible = ref<boolean>(false)
const currentType = ref<Type | null>(null)
const childTypes = ref<Type[]>([])

const showChildTypes = (row: Type) => {
  if (row.id !== undefined) {
    currentType.value = row
    loading.value = true
    getChildTypes(row.id)
      .then(({ data }) => {
        childTypes.value = data
        childrenDialogVisible.value = true
      })
      .catch(() => {
        childTypes.value = []
      })
      .finally(() => {
        loading.value = false
      })
  }
}

// 获取子类型列表
const getChildTypes = (id: number): Promise<{ data: Type[] }> => {
  return new Promise((resolve) => {
    const data = tableData.value.filter(() => formData.parentTypeIds.includes(id as number))
    resolve({ data })
  })
}
//#endregion

// 监听分页参数的变化
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="keyword" label="关键字">
          <el-input v-model="searchData.keyword" placeholder="请输入类型名称" />
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
          <el-button type="primary" :icon="CirclePlus" @click="handleCreate">新增类型</el-button>
          <el-button type="danger" :icon="Delete">批量删除</el-button>
        </div>
        <div>
          <el-tooltip content="刷新当前页">
            <el-button type="primary" :icon="RefreshRight" circle @click="getTableData" />
          </el-tooltip>
        </div>
      </div>
      <div class="table-wrapper">
        <el-table :data="tableData">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column prop="id" label="类型ID" align="center" />
          <el-table-column prop="name" label="类型名称" align="center" />
          <el-table-column prop="description" label="类型描述" align="center" />
          <el-table-column fixed="right" label="操作" width="250" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="info" text bg size="small" @click="showChildTypes(scope.row)">子类型(后补)</el-button>
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

    <!-- 新增/修改类型对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="40%" @closed="resetForm">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="类型名称" prop="type.name">
          <el-input v-model="formData.type.name" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="类型描述">
          <el-input v-model="formData.type.description" type="textarea" :rows="4" placeholder="请输入类型描述" />
        </el-form-item>
        <el-form-item label="父类型">
          <el-select
            v-model="formData.parentTypeIds"
            multiple
            filterable
            clearable
            placeholder="请选择父类型"
            style="width: 100%"
          >
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id!"
              :disabled="item.id === formData.type.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 子类型对话框 -->
    <el-dialog v-model="childrenDialogVisible" title="子类型列表" width="60%">
      <div v-if="currentType">
        <h3>{{ currentType.name }} 的子类型列表</h3>
        <el-table :data="childTypes" style="width: 100%">
          <el-table-column prop="id" label="类型ID" align="center" />
          <el-table-column prop="name" label="类型名称" align="center" />
          <el-table-column prop="description" label="类型描述" align="center" />
        </el-table>
        <div v-if="childTypes.length === 0" style="text-align: center; padding: 20px">暂无子类型</div>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.search-wrapper {
  margin-bottom: 20px;
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
  justify-content: center;
}
</style>
