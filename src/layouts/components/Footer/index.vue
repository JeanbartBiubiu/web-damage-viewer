<script lang="ts" setup>
const VITE_APP_TITLE = import.meta.env.VITE_APP_TITLE
</script>

<template>
  <footer class="layout-footer">
    <p>
      <a href="https://beian.miit.gov.cn/" target="_blank">粤ICP备2025393185号-1</a>
    </p>
    <p>
      <img src="https://beian.mps.gov.cn/img/logo01.dd7ff50e.png" width="20" />
      <a href="https://beian.mps.gov.cn/#/query/webSearch?code=44030002006529" rel="noreferrer" target="_blank"
        >粤公网安备44030002006529号</a
      >
    </p>
  </footer>
</template>

<style lang="scss" scoped>
.layout-footer {
  width: 100%;
  min-height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-text-color-placeholder);
}
.layout-footer p {
  display: block;
  margin: 0; // 去除默认的上下外边距
}
</style>
