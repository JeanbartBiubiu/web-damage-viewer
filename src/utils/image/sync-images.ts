/**
 * 图片同步工具
 * 用于从服务器获取图片列表并同步到本地IndexDB
 */

import { getImage } from "@/api/image"
import { imageManager } from "./image_manager"
import type { Image } from "@/api/image/types/image"
import { getCurrentSchema } from "@/utils/cache/cookies"

/**
 * 将服务器图片数据转换为IndexDB格式
 */
function convertToIndexDBFormat(images: Image[]) {
  return images.map((img) => ({
    uri: getCurrentSchema() + "_" + img.uri,
    image: img.image,
    create_time: img.createTime || new Date().toISOString(),
    update_time: img.updateTime || new Date().toISOString()
  }))
}

/**
 * 从服务器同步所有图片到IndexDB
 * @returns 同步的图片数量
 */
export async function syncImagesFromServer(): Promise<number> {
  try {
    // 从服务器获取所有图片
    const response = await getImage()
    if (!response.data) {
      console.error("同步图片失败: 服务器返回数据格式错误")
      return 0
    }

    const images = response.data.list
    if (!images || images.length === 0) {
      console.log("没有图片需要同步")
      return 0
    }

    // 转换为IndexDB格式并保存
    const indexDBImages = convertToIndexDBFormat(images)
    const count = await imageManager.batchInsert(indexDBImages)
    console.log(`成功同步 ${count} 张图片到本地数据库`)
    return count
  } catch (error) {
    console.error("同步图片失败:", error)
    return 0
  }
}

/**
 * 根据URI列表同步指定图片
 * @param uris 图片URI列表
 * @returns 同步的图片数量
 */
export async function syncImagesByUris(uris: string[]): Promise<number> {
  if (!uris || uris.length === 0) {
    return 0
  }

  let syncCount = 0
  const errors: string[] = []

  // 逐个同步图片
  for (const uri of uris) {
    try {
      // 这里可以根据实际API调整，如果有批量获取接口可以优化
      const response = await getImage()
      const images = response.data?.list || []
      const image = images.find((img) => img.uri === uri)

      if (image) {
        const indexDBImages = convertToIndexDBFormat([image])
        await imageManager.batchInsert(indexDBImages)
        syncCount++
      } else {
        errors.push(`未找到图片: ${uri}`)
      }
    } catch (error) {
      console.error(`同步图片失败 (URI: ${uri}):`, error)
      errors.push(uri)
    }
  }

  if (errors.length > 0) {
    console.warn(`${errors.length} 张图片同步失败:`, errors)
  }

  return syncCount
}
