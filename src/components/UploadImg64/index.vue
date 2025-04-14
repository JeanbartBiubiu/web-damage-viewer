<template>
  <el-upload
    class="avatar-uploader"
    action="#"
    accept="image/jpeg,image/png,image/jpg"
    :auto-upload="false"
    :show-file-list="false"
    :on-change="handleUpload"
  >
    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon">
      <Plus />
    </el-icon>
  </el-upload>
</template>

<script lang="ts" setup>
import { ref } from "vue"
import { Plus } from "@element-plus/icons-vue"
import { Jimp } from "jimp"

import type { UploadProps } from "element-plus"

// 定义 props 和 emits
const props = defineProps<{
  img: string | undefined
}>()
const emits = defineEmits(["update:img"])

// 使用 props 中的 modelValue 作为初始值
const imageUrl = ref(props.img)

const handleUpload: UploadProps["onChange"] = (uploadFile) => {
  const file = uploadFile.raw!
  const reader = new FileReader()
  console.log(file)
  reader.onload = async (e) => {
    console.log(e)
    if (e.target?.result) {
      const image = await Jimp.read(e.target.result as ArrayBuffer)
      // 裁切成正方形
      const height = image.height
      const width = image.width
      if (height > width) {
        image.crop({ x: 0, y: 0, h: width, w: width })
      } else {
        image.crop({ x: 0, y: 0, h: height, w: height })
      }
      // 调整大小为64*64
      image.resize({ w: 64, h: 64 })
      const base64 = await image.getBase64("image/jpeg")
      imageUrl.value = base64
      // 触发 update:model-value 事件，更新父组件的 v-model 值
      emits("update:img", imageUrl.value)
      console.log(imageUrl.value)
      console.log("----")
      console.log(props.img)
    }
  }
  reader.readAsArrayBuffer(file)
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
