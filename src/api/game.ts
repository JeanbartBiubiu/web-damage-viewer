import { request } from "@/utils/service"

export interface GameInfo {
  gamdId?: string
  gameCode?: string
  gameName?: string
}

export interface GameInfoResponse {
  list: GameInfo[];
}


export function getGameInfo(): Promise<{ data: GameInfoResponse }> {
  return request({
    url: "gameInfo",
    method: "get",
  });
}
