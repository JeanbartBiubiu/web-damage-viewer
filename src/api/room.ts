import { request } from "@/utils/service"

export function getRooms(): Promise<{ data: string[] }> {
  return request({
    url: "rooms",
    method: "get"
  })
}
