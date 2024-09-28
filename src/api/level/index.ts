import { request } from "@/utils/service"
import type * as Level from "./types/level"


/** 增 */
export function createLevel(data: Level.LevelRequestData) {
  return request({
    url: "level",
    method: "post",
    data
  })
}

/** 删 */
export function deleteLevel(id: number) {
  return request({
    url: `level/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateLevel(data: Level.LevelRequestData) {
  return request({
    url: "level",
    method: "put",
    data
  })
}

/** 查 */
export function getLevel(params: Level.LevelRequestData) {
  return request<Level.GetLevelResponseData>({
    url: "level",
    method: "get",
    params
  })
}

