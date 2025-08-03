/**
 * 图片管理工具类
 * 用于管理图片数据，提供批量插入和获取图片的功能
 */

// 定义图片数据接口
interface ImageData {
  uri: string
  create_time: string
  update_time: string
  image: string
}

// IndexedDB数据库名称和版本
const DB_NAME = "image_db"
const DB_VERSION = 1
const STORE_NAME = "images"

/**
 * 图片管理器类
 */
export class ImageManager {
  private db: IDBDatabase | null = null
  private dbReady: Promise<boolean>
  private dbReadyResolve!: (value: boolean) => void

  constructor() {
    this.dbReady = new Promise<boolean>((resolve) => {
      this.dbReadyResolve = resolve
    })
    this.initDB()
  }

  /**
   * 初始化IndexedDB数据库
   */
  private initDB(): void {
    const request = indexedDB.open(DB_NAME, DB_VERSION)

    request.onerror = (event) => {
      console.error("数据库打开失败:", event)
      this.dbReadyResolve(false)
    }

    request.onsuccess = (event) => {
      this.db = (event.target as IDBOpenDBRequest).result
      console.log("数据库连接成功")
      this.dbReadyResolve(true)
    }

    request.onupgradeneeded = (event) => {
      const db = (event.target as IDBOpenDBRequest).result

      // 如果存储对象不存在，则创建新的存储对象
      if (!db.objectStoreNames.contains(STORE_NAME)) {
        // 使用uri作为键
        const objectStore = db.createObjectStore(STORE_NAME, { keyPath: "uri" })

        // 创建索引
        objectStore.createIndex("uri", "uri", { unique: true })
        objectStore.createIndex("create_time", "create_time", { unique: false })
        objectStore.createIndex("update_time", "update_time", { unique: false })

        console.log("数据库表结构创建成功")
      }
    }
  }

  /**
   * 等待数据库准备就绪
   */
  private async waitForDB(): Promise<boolean> {
    return this.dbReady
  }

  /**
   * 批量插入或更新图片数据
   * @param images 图片数据数组
   * @returns 成功插入的数量
   */
  public async batchInsert(images: ImageData[]): Promise<number> {
    const isReady = await this.waitForDB()
    if (!isReady || !this.db) {
      throw new Error("数据库未就绪")
    }

    return new Promise<number>((resolve, reject) => {
      try {
        const transaction = this.db!.transaction(STORE_NAME, "readwrite")
        const store = transaction.objectStore(STORE_NAME)
        let successCount = 0

        transaction.oncomplete = () => {
          console.log(`成功插入/更新 ${successCount} 条图片数据`)
          resolve(successCount)
        }

        transaction.onerror = (event) => {
          console.error("事务错误:", event)
          reject(new Error("批量插入过程中发生错误"))
        }

        // 逐个插入或更新图片数据
        images.forEach((image) => {
          const request = store.put(image) // put会覆盖已存在的记录

          request.onsuccess = () => {
            successCount++
          }

          request.onerror = (event) => {
            console.error("插入/更新图片数据失败:", event)
          }
        })
      } catch (error) {
        console.error("批量插入操作失败:", error)
        reject(error)
      }
    })
  }

  /**
   * 获取所有图片数据
   * @returns 图片数据数组
   */
  public async getAllImages(): Promise<ImageData[]> {
    const isReady = await this.waitForDB()
    if (!isReady || !this.db) {
      throw new Error("数据库未就绪")
    }

    return new Promise<ImageData[]>((resolve, reject) => {
      try {
        const transaction = this.db!.transaction(STORE_NAME, "readonly")
        const store = transaction.objectStore(STORE_NAME)
        const request = store.getAll()

        request.onsuccess = () => {
          resolve(request.result as ImageData[])
        }

        request.onerror = (event) => {
          console.error("获取图片数据失败:", event)
          reject(new Error("获取图片数据失败"))
        }
      } catch (error) {
        console.error("获取所有图片操作失败:", error)
        reject(error)
      }
    })
  }

  /**
   * 根据关键字模糊查询 uri
   * @param keyword 关键字（大小写敏感）
   * @returns 命中的图片数据数组
   */
  public async searchImagesLike(keyword: string): Promise<ImageData[]> {
    const all = await this.getAllImages()
    // 空关键字则返回全部
    if (!keyword.trim()) return all
    return all.filter((item) => item.uri.includes(keyword))
  }

  /**
   * 根据URI获取单个图片数据
   * @param uri 图片URI
   * @returns 图片数据或null（如果不存在）
   */
  public async getImageByUri(uri: string): Promise<string | null> {
    const isReady = await this.waitForDB()
    if (!isReady || !this.db) {
      throw new Error("数据库未就绪")
    }

    return new Promise<string | null>((resolve, reject) => {
      try {
        const transaction = this.db!.transaction(STORE_NAME, "readonly")
        const store = transaction.objectStore(STORE_NAME)
        const request = store.get(uri)

        request.onsuccess = () => {
          // 检查 result 是否存在，避免访问 undefined 的属性
          if (request.result) {
            resolve((request.result as ImageData).image || null)
          } else {
            // 未找到记录，返回 null
            resolve(null)
          }
        }

        request.onerror = (event) => {
          console.error(`获取图片数据失败 (URI: ${uri}):`, event)
          reject(new Error(`获取图片数据失败 (URI: ${uri})`))
        }
      } catch (error) {
        console.error(`获取图片操作失败 (URI: ${uri}):`, error)
        reject(error)
      }
    })
  }
}

// 导出单例实例
export const imageManager = new ImageManager()
