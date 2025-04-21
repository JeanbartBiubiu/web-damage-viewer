<script lang="ts" setup>
import { computed, watchEffect, onMounted } from "vue"
import { storeToRefs } from "pinia"
import { useSettingsStore } from "@/store/modules/settings"
import useResize from "./hooks/useResize"
import { useWatermark } from "@/hooks/useWatermark"
import { useDevice } from "@/hooks/useDevice"
import { useLayoutMode } from "@/hooks/useLayoutMode"
import LeftMode from "./LeftMode.vue"
import TopMode from "./TopMode.vue"
import LeftTopMode from "./LeftTopMode.vue"
import { Settings, RightPanel } from "./components"
import { getCssVariableValue, setCssVariableValue } from "@/utils"
import { request } from "@/utils/service"

/** Layout 布局响应式 */
useResize()

const { setWatermark, clearWatermark } = useWatermark()
const { isMobile } = useDevice()
const { isLeft, isTop, isLeftTop } = useLayoutMode()
const settingsStore = useSettingsStore()
const { showSettings, showTagsView, showWatermark, showGreyMode, showColorWeakness } = storeToRefs(settingsStore)

const classes = computed(() => {
  return {
    showGreyMode: showGreyMode.value,
    showColorWeakness: showColorWeakness.value
  }
})

//#region 隐藏标签栏时删除其高度，是为了让 Logo 组件高度和 Header 区域高度始终一致
const cssVariableName = "--v3-tagsview-height"
const v3TagsviewHeight = getCssVariableValue(cssVariableName)
watchEffect(() => {
  showTagsView.value
    ? setCssVariableValue(cssVariableName, v3TagsviewHeight)
    : setCssVariableValue(cssVariableName, "0px")
})
//#endregion

/** 开启或关闭系统水印 */
watchEffect(() => {
  showWatermark.value ? setWatermark(import.meta.env.VITE_APP_TITLE) : clearWatermark()
})

/** 记录PV和UV统计 */
const recordPvUv = () => {
  // 获取或生成用户唯一标识符
  let uid = localStorage.getItem("visitor_uid")
  if (!uid) {
    uid = `user_${Math.random().toString(36).substring(2, 15)}_${Date.now()}`
    localStorage.setItem("visitor_uid", uid)
  }

  // 发送请求记录PV和UV
  request({
    url: "/pv",
    method: "get",
    params: { uid }
  }).catch((error) => {
    console.error("统计PV、UV失败:", error)
  })
}

// 在组件挂载时记录PV和UV
onMounted(() => {
  recordPvUv()
})
</script>

<template>
  <div :class="classes">
    <!-- 左侧模式 -->
    <LeftMode v-if="isLeft || isMobile" />
    <!-- 顶部模式 -->
    <TopMode v-else-if="isTop" />
    <!-- 混合模式 -->
    <LeftTopMode v-else-if="isLeftTop" />
    <!-- 右侧设置面板 -->
    <RightPanel v-if="showSettings">
      <Settings />
    </RightPanel>
  </div>
</template>

<style lang="scss" scoped>
.showGreyMode {
  filter: grayscale(1);
}

.showColorWeakness {
  filter: invert(0.8);
}
</style>
