<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { createEquipment, deleteEquipment, updateEquipment, getEquipment, getEquipmentDetail } from "@/api/equipment"
import { type EquipmentRequestData, type GetEquipmentData } from "@/api/equipment/types/equipment"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { cloneDeep } from "lodash-es"
import UploadImg64 from "@/components/UploadImg64/index.vue"
import EquipmentValueSetting from "@/components/EquipmentValueSetting/index.vue"

defineOptions({
  // 命名当前组件
  name: "EquipmentMge"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: EquipmentRequestData = {
  equipmentId: undefined,
  equipmentName: "",
  equipmentImg: "",
  type: undefined,
  equipIds: []
}
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formData = ref<EquipmentRequestData>(cloneDeep(DEFAULT_FORM_DATA))
const formRules: FormRules<EquipmentRequestData> = {
  equipmentName: [{ required: true, trigger: "blur", message: "请输入装备名" }],
  equipmentImg: [{ required: false, trigger: "blur", message: "请上传装备图片" }]
}
const handleCreateOrUpdate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    loading.value = true
    const api = formData.value.equipmentId === undefined ? createEquipment : updateEquipment
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
const handleDelete = (row: GetEquipmentData) => {
  ElMessageBox.confirm(`正在删除装备：${row.equipmentName}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteEquipment(row.equipmentId).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const handleUpdate = (row: GetEquipmentData) => {
  resetForm()
  dialogVisible.value = true
  loading.value = true

  getEquipmentDetail(row.equipmentId)
    .then(({ data }) => {
      formData.value = {
        equipmentId: data.equipmentId,
        equipmentName: data.equipmentName,
        consumption: data.consumption,
        attributeExpression: data.attributeExpression,
        subEquips: data.subEquips,
        subEquipsModel: data.subEquipsModel,
        equipmentImg: data.equipmentImg,
        type: data.type,
        equipIds: data.equipIds || []
      }
    })
    .catch(() => {
      ElMessage.error("获取装备详情失败")
    })
    .finally(() => {
      console.log("editData 数据:", formData.value)
      loading.value = false
    })
}
//#endregion

//#region 查
const tableData = ref<GetEquipmentData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  equipmentId: 0,
  equipmentName: "",
  type: 1
})
const getTableData = () => {
  loading.value = true
  getEquipment({
    // currentPage: paginationData.currentPage,
    // size: paginationData.pageSize,
    equipmentId: searchData.equipmentId || undefined,
    equipmentName: searchData.equipmentName || "",
    type: searchData.type || undefined
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

const updateImg = (img: string) => {
  formData.value.equipmentImg = img
}
//#endregion

/** 监听分页参数的变化 */
watch([() => paginationData.currentPage, () => paginationData.pageSize], getTableData, { immediate: true })
</script>

<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="searchFormRef" :inline="true" :model="searchData">
        <el-form-item prop="equipmentId" label="属性id">
          <el-input v-model="searchData.equipmentId" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="equipmentName" label="属性名">
          <el-input v-model="searchData.equipmentName" placeholder="请输入" />
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
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增装备</el-button>
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
          <el-table-column prop="equipmentImg" width="128" label="图片" align="center">
            <template #default="scope">
              <el-image
                :src="scope.row.equipmentImg"
                :preview-src-list="[scope.row.equipmentImg]"
                fit="cover"
                style="width: 50px; height: 50px; border-radius: 4px"
              />
            </template>
          </el-table-column>
          <el-table-column prop="equipmentId" label="装备id" align="center" />
          <el-table-column prop="equipmentName" label="装备名称" align="center" />
          <el-table-column fixed="right" label="操作" width="150" align="center">
            <template #default="scope">
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">修改</el-button>
              <el-button type="primary" text bg size="small" @click="handleUpdate(scope.row)">绑定技能效果</el-button>
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
      :title="formData.equipmentId === undefined ? '新增属性' : '修改属性'"
      @closed="resetForm"
      width="30%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item prop="equipmentName" label="装备名">
          <el-input v-model="formData.equipmentName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="equipmentImg" label="头像">
          <UploadImg64 :img="formData.equipmentImg" @update:img="updateImg" />
        </el-form-item>
        <el-form-item prop="consumption" label="价格">
          <el-input-number v-model="formData.consumption" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="attributeExpression" label="属性加成">
          <el-input type="textarea" v-model="formData.attributeExpression" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="subEquips" label="子装备">
          <el-input v-model="formData.subEquipsModel" placeholder="请选择" />
        </el-form-item>
        <el-form-item>
          <EquipmentValueSetting
            :equipId="formData.equipmentId || 0"
            :editData="formData.equipIds"
            @update:values="(newValues) => (formData.equipIds = newValues)"
          />
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
