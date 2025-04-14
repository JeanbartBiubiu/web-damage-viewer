import { request } from "@/utils/service"

export interface EncryptedData {
  ephemeralPublicKey: string
  iv: string
  data: string
}

export interface GetTokenData {
  proof: EncryptedData
  timestamp: number
}

export function getAToken(data: GetTokenData): Promise<{ data: string }> {
  return request({
    url: "/handshake/getAToken",
    method: "post",
    data
  })
}
