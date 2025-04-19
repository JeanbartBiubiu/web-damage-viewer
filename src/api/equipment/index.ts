import { request } from "@/utils/service"
import type * as Equipment from "./types/equipment"
import { GetEquipmentResponseDetail } from "./types/equipment"

/** 增 */
export function createEquipment(data: Equipment.EquipmentRequestData) {
  return request({
    url: "equipment",
    method: "post",
    data
  })
}

/** 删 */
export function deleteEquipment(id: number) {
  return request({
    url: `equipment/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateEquipment(data: Equipment.EquipmentRequestData) {
  return request({
    url: "equipment",
    method: "put",
    data
  })
}

/** 查 */
export function getEquipment(params: Equipment.EquipmentRequestData) {
  return request<Equipment.GetEquipmentResponseData>({
    url: "equipment",
    method: "get",
    params
  })
}

/** 获取装备详情 */
export function getEquipmentDetail(id: number) {
  return request<Equipment.GetEquipmentResponseDetail>({
    url: `equipment/${id}`,
    method: "get"
  })
}
