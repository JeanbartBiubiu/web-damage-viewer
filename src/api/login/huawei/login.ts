import { request } from "@/utils/service"
import { SSOBody } from "@/api/login/huawei/types/login"

export function getToken(data: SSOBody): Promise<{ data: string }> {
  return request({
    url: "/login/huawei/token",
    method: "post",
    data
  })
}
