import { request } from "@/utils/service"
import type * as LifeCycle from "./types/lifeCycle"


/** 增 */
export function createLifeCycle(data: LifeCycle.LifeCycleRequestData) {
  return request({
    url: "lifeCycle",
    method: "post",
    data
  })
}

/** 删 */
export function deleteLifeCycle(id?: number) {
  return request({
    url: `lifeCycle/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateLifeCycle(data: LifeCycle.LifeCycleRequestData) {
  return request({
    url: "lifeCycle",
    method: "put",
    data
  })
}

/** 查 */
export function getLifeCycle(params: LifeCycle.LifeCycleRequestData) {
  return request<LifeCycle.GetLifeCycleResponseData>({
    url: "lifeCycle",
    method: "get",
    params
  })
}

