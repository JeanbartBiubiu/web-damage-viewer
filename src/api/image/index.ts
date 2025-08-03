import type { Image } from "./types/image"
import { request } from "@/utils/service"
import { GetImageResponseData } from "./types/image"

export function getImage() {
  return request<GetImageResponseData>({
    url: "image",
    method: "get"
  })
}

export function getImageByUri(uri: string) {
  return request<Image>({
    url: `image/${uri}`,
    method: "get"
  })
}

export function updateImage(data: Image) {
  return request<String>({
    url: "image",
    method: "post",
    data
  })
}
