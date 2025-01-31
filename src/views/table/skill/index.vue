<script lang="ts" setup>
import { reactive, ref, watch } from "vue"
import { createSkill, deleteSkill, updateSkill, getSkill } from "@/api/skill"
import { type SkillRequestData, type GetSkillData } from "@/api/skill/types/skill"
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"
import { Search, Refresh, CirclePlus, Delete, Download, RefreshRight } from "@element-plus/icons-vue"
import { usePagination } from "@/hooks/usePagination"
import { cloneDeep } from "lodash-es"
import ConditionEditor from "@/components/ConditionEditor/index.vue"

defineOptions({
  // 命名当前组件
  name: "SkillMge"
})

const loading = ref<boolean>(false)
const { paginationData, handleCurrentChange, handleSizeChange } = usePagination()

//#region 增
const DEFAULT_FORM_DATA: SkillRequestData = {
  skillId: undefined,
  skillLevel: 1,
  skillName: "",
  skillImg: "",
  skillType: undefined,
  skillConsumption: "100",
  skillCd: "100",
  skillCheckAndDo: "0",
  skillCastPoint: "0.1",
  skillCastPointAfter: "0.1"
}
const dialogVisible = ref<boolean>(false)
const formRef = ref<FormInstance | null>(null)
const formData = ref<
  SkillRequestData & {
    conditions: Array<{
      conditions: Array<{
        variable: string
        operator: string
        value: string
        logic?: string
      }>
      actions: Array<{
        property: string
        expression: string
        value: string
      }>
    }>
  }
>(
  cloneDeep({
    ...DEFAULT_FORM_DATA,
    conditions: []
  })
)
const formRules: FormRules<SkillRequestData> = {
  skillName: [{ required: true, trigger: "blur", message: "请输入技能名" }],
  skillImg: [{ required: false, trigger: "blur", message: "请上传技能图片" }]
}
const handleCreateOrUpdate = () => {
  formRef.value?.validate((valid: boolean, fields) => {
    if (!valid) return console.error("表单校验不通过", fields)
    loading.value = true
    const api = formData.value.skillId === undefined ? createSkill : updateSkill
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
  formData.value = cloneDeep({
    ...DEFAULT_FORM_DATA,
    conditions: []
  })
}
//#endregion

//#region 删
const handleDelete = (row: GetSkillData) => {
  ElMessageBox.confirm(`正在删除技能：${row.skillName}，确认删除？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    deleteSkill(row.skillId).then(() => {
      ElMessage.success("删除成功")
      getTableData()
    })
  })
}
//#endregion

//#region 改
const handleUpdate = (row: GetSkillData) => {
  dialogVisible.value = true
  formData.value = cloneDeep(row)
}
//#endregion

//#region 查
const tableData = ref<GetSkillData[]>([])
const searchFormRef = ref<FormInstance | null>(null)
const searchData = reactive({
  skillId: 0,
  skillName: "",
  skillType: 1
})
const getTableData = () => {
  loading.value = true
  getSkill({
    // currentPage: paginationData.currentPage,
    // size: paginationData.pageSize,
    skillId: searchData.skillId || undefined,
    skillName: searchData.skillName || "",
    skillType: searchData.skillType || undefined
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
        <el-form-item prop="skillId" label="技能id">
          <el-input v-model="searchData.skillId" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="skillName" label="技能名">
          <el-input v-model="searchData.skillName" placeholder="请输入" />
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
          <el-button type="primary" :icon="CirclePlus" @click="dialogVisible = true">新增技能</el-button>
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
          <el-table-column prop="skillImg" label="图标" align="center" />
          <el-table-column prop="skillId" label="技能id" align="center" />
          <el-table-column prop="skillName" label="技能名称" align="center" />
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
      :title="formData.skillId === undefined ? '新增技能' : '修改技能'"
      @closed="resetForm"
      width="30%"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" label-position="left">
        <el-form-item prop="skillLevel" label="技能等级">
          <el-input v-model="formData.skillLevel" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="skillName" label="技能名">
          <el-input v-model="formData.skillName" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="skillImg" label="技能图片">
          <el-input v-model="formData.skillImg" placeholder="请上传" />
        </el-form-item>
        <el-form-item prop="skillConsumption" label="技能消耗">
          <el-input v-model="formData.skillConsumption" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="skillCd" label="冷却时间">
          <el-input v-model="formData.skillCd" placeholder="请输入" />
        </el-form-item>
        <!-- 一个高级组件，可以新增，删除 -->
        <el-form-item prop="skillCheckAndDo" label="技能详情">
          <condition-editor v-model="formData.conditions" />
        </el-form-item>
        <el-form-item prop="skillCastPoint" label="前摇">
          <el-input v-model="formData.skillCastPoint" />
        </el-form-item>
        <el-form-item prop="skillCastPointAfter" label="后摇">
          <el-input v-model="formData.skillCastPointAfter" />
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
