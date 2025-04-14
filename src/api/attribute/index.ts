import { request } from "@/utils/service"
import type * as Attribute from "./types/attribute"

/** 增 */
export function createAttribute(data: Attribute.AttributeRequestData) {
  return request({
    url: "attribute",
    method: "post",
    data
  })
}

/** 删 */
export function deleteAttribute(id: number) {
  return request({
    url: `attribute/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateAttribute(data: Attribute.AttributeRequestData) {
  return request({
    url: "attribute",
    method: "put",
    data
  })
}

/** 查 */
export function getAttribute(params: Attribute.AttributeRequestData) {
  return request<Attribute.GetAttributeResponseData>({
    url: "attribute",
    method: "get",
    params
  })
}
