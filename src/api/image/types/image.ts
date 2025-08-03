export interface Image {
  uri: string
  createTime: string | undefined
  updateTime: string | undefined
  image: string
}

export type GetImageResponseData = ApiResponseData<{
  list: Image[]
  total: number
}>
