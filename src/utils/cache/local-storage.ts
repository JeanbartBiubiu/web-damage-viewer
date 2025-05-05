/** 统一处理 localStorage */

import CacheKey from "@/constants/cache-key"
import { type SidebarOpened, type SidebarClosed } from "@/constants/app-key"
import { type ThemeName } from "@/hooks/useTheme"
import { type TagView } from "@/store/modules/tags-view"
import { type LayoutSettings } from "@/config/layouts"
import { jwtVerify } from "jose"
import { ElMessage } from "element-plus"

const PUBLIC_KEY = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8U7rh4gVzCjqXBOisIN9
CREq8nYHaa1D8rsyIXQ+tbrcL3ZonzfVSA4VA3LcJSMP7383BlWDbz23Vrmb7ouo
5s5370+yh0AVWp5ADr+h+2kO8tOQjmFSGuEilaNrlQRkHpiugkJUYj2+EOhv/tD5
lmJNTHVLY+x943qTZMSAJoqZUzOWd2RZWGIcLaxAu1gld47syBiTeoMo6RWDKFB1
jkhb0krOQ6EXi3VAuL1POXjlCIaGutcIaHRYUVG7l5lWYHzNnGXDyahA73GolOoc
GN1baZ1hnU66UW0Lq27P4Icu8ShjBApda4Izdtuxt6RALFbIWEXrFhAzpP2TU9WM
aQIDAQAB
-----END PUBLIC KEY-----`

const getPublicKey = async () => {
  // 移除 PEM 头和尾，并去除空格
  const base64PublicKey = PUBLIC_KEY.replace(/-----BEGIN PUBLIC KEY-----|-----END PUBLIC KEY-----/g, "").replace(
    /\s/g,
    ""
  )
  // 将 Base64 编码的公钥转换为 Uint8Array
  const publicKeyBuffer = Uint8Array.from(atob(base64PublicKey), (c) => c.charCodeAt(0))

  return await crypto.subtle.importKey(
    "spki",
    publicKeyBuffer,
    {
      name: "RSASSA-PKCS1-v1_5",
      hash: "SHA-256"
    },
    true,
    ["verify"]
  )
}

//#region 系统布局配置
export const getConfigLayout = () => {
  const json = localStorage.getItem(CacheKey.CONFIG_LAYOUT)
  return json ? (JSON.parse(json) as LayoutSettings) : null
}
export const setConfigLayout = (settings: LayoutSettings) => {
  localStorage.setItem(CacheKey.CONFIG_LAYOUT, JSON.stringify(settings))
}
export const removeConfigLayout = () => {
  localStorage.removeItem(CacheKey.CONFIG_LAYOUT)
}
//#endregion

//#region 侧边栏状态
export const getSidebarStatus = () => {
  return localStorage.getItem(CacheKey.SIDEBAR_STATUS)
}
export const setSidebarStatus = (sidebarStatus: SidebarOpened | SidebarClosed) => {
  localStorage.setItem(CacheKey.SIDEBAR_STATUS, sidebarStatus)
}
//#endregion

//#region 正在应用的主题名称
export const getActiveThemeName = () => {
  return localStorage.getItem(CacheKey.ACTIVE_THEME_NAME) as ThemeName | null
}
export const setActiveThemeName = (themeName: ThemeName) => {
  localStorage.setItem(CacheKey.ACTIVE_THEME_NAME, themeName)
}
//#endregion

//#region 标签栏
export const getVisitedViews = () => {
  const json = localStorage.getItem(CacheKey.VISITED_VIEWS)
  return JSON.parse(json ?? "[]") as TagView[]
}
export const setVisitedViews = (views: TagView[]) => {
  views.forEach((view) => {
    // 删除不必要的属性，防止 JSON.stringify 处理到循环引用
    delete view.matched
    delete view.redirectedFrom
  })
  localStorage.setItem(CacheKey.VISITED_VIEWS, JSON.stringify(views))
}
export const getCachedViews = () => {
  const json = localStorage.getItem(CacheKey.CACHED_VIEWS)
  return JSON.parse(json ?? "[]") as string[]
}
export const setCachedViews = (views: string[]) => {
  localStorage.setItem(CacheKey.CACHED_VIEWS, JSON.stringify(views))
}
//#endregion

//#region token 相关
// 解析 JWT 荷载部分
// 解析 JWT 荷载部分
const parseJwtPayload = (token: string) => {
  try {
    const payloadBase64 = token.split(".")[1]
    const decodedPayload = atob(payloadBase64)
    console.log("JWT 荷载:", decodedPayload)
    return JSON.parse(decodedPayload)
  } catch (error) {
    console.error("解析 JWT 失败:", error)
    return null
  }
}

// 验证 JWT 签名
const verifyJwtSignature = async (token: string): Promise<boolean> => {
  try {
    const publicKey = await getPublicKey()
    await jwtVerify(token, publicKey)
    return true
  } catch (error) {
    removeJwtToken()
    ElMessage.error("token校验不通过，请重新登录以更新凭据")
    return false
  }
}

// 获取存储的 JWT
export const getJwtToken = () => {
  const jwt = localStorage.getItem(CacheKey.JWT_TOKEN)
  if (jwt) {
    const payload = parseJwtPayload(jwt)
    if (payload && isJwtExpired(payload)) {
      removeJwtToken()
      ElMessage.error("登录已过期，请重新登录")
      return null
    }
  }
  return jwt
}
// 获取用于后端鉴权的 token
export const getAuthToken = async () => {
  const jwt = getJwtToken()
  if (jwt && (await verifyJwtSignature(jwt))) {
    const payload = parseJwtPayload(jwt)
    return payload?.sub || ""
  }
  return ""
}

const isJwtExpired = (payload: any) => {
  if (payload && payload.exp) {
    const currentTime = Math.floor(Date.now() / 1000)
    return currentTime > payload.exp
  }
  return false
}

// 移除存储的 JWT
export const removeJwtToken = () => {
  localStorage.removeItem(CacheKey.JWT_TOKEN)
}

// 获取 canEdit 字段
export const getCanEdit = async () => {
  const jwt = getJwtToken()
  if (jwt && (await verifyJwtSignature(jwt))) {
    const payload = parseJwtPayload(jwt)
    return payload?.canEdit || false
  }
  return false
}

// 获取 pay 字段用于前端鉴权
export const getPayAuth = async () => {
  const jwt = getJwtToken()
  if (jwt && (await verifyJwtSignature(jwt))) {
    const payload = parseJwtPayload(jwt)
    return payload?.pay || null
  }
  return null
}
//#endregion
