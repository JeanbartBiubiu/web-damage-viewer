import { request } from "@/utils/service"
import type * as Indiv from "./types/individual"


/** 增 */
export function createIndiv(data: Indiv.IndivRequestData) {
  return request({
    url: "indiv",
    method: "post",
    data
  })
}

/** 删 */
export function deleteIndiv(id: number) {
  return request({
    url: `indiv/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateIndiv(data: Indiv.IndivRequestData) {
  return request({
    url: "indiv",
    method: "put",
    data
  })
}

/** 查 */
export function getIndiv(params: Indiv.IndivRequestData) {
  return request<Indiv.GetIndivResponseData>({
    url: "indiv",
    method: "get",
    params
  })
}

// 获取值
/** 查 */
export function getIndivdualValues(params: Indiv.IndivRequestData) {
  return request<Indiv.GetIndivValueResponseData>({
    url: "indiv/values",
    method: "get",
    params
  })
}

export function settingIndivAttrValues(data: Indiv.IndivAttributeValueRequestData[]) {
  return request<boolean>({
    url: "indiv/values",
    method: "put",
    data
  })
}

