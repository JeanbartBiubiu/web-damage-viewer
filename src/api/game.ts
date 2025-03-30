import { request } from "@/utils/service"

export interface GameInfo {
  id?: string
  username: string
  password?: string
}


export function getGameInfo() {
  return request({
    url: "gameInfo",
    method: "get",
  })
}
