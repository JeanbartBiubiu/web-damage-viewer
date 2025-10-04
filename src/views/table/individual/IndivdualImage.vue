<script lang="ts" setup>
import { ref, onMounted } from "vue"
import { getCurrentSchema } from "@/utils/cache/cookies"
import { imageManager } from "@/utils/image/image_manager"

const props = defineProps<{
  equipmentId: number
}>()

const src = ref<string>("")

onMounted(async () => {
  const uri = `${getCurrentSchema()}_individual_${props.equipmentId}`
  try {
    const data = await imageManager.getImageByUri(uri)
    src.value = data || ""
  } catch {
    src.value = ""
  }
})
</script>

<template>
  <el-image
    :src="src"
    :preview-src-list="src ? [src] : []"
    fit="cover"
    style="width: 50px; height: 50px; border-radius: 4px"
  />
</template>
