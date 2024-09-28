export interface AttributeRequestData {
  attributeId?: number
  attributeName?: string
  attributeImg?: string
  type?: number
}

export interface GetAttributeRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：用户名 */
  attributeId?: string
  /** 查询参数：手机号 */
  attributeName?: string
}

export interface GetAttributeData {
  attributeId: number
  attributeName: string
  attributeImg: string
  type: number
}

export type GetAttributeResponseData = ApiResponseData<{
  list: GetAttributeData[]
  total: number
}>
