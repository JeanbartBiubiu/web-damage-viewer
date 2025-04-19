export interface EquipmentRequestData {
  equipmentId?: number
  equipmentName?: string
  consumption?: number // 价格
  attributeExpression?: string // 属性加成表达式
  subEquips?: Array<string>
  subEquipsModel?: string
  equipmentImg?: string
  type?: number
  equipIds?: Array<EquipmentValue>
}

export interface EquipmentValue {
  equipId: number
  attributeId: number
  addValue: number
  multiValue: number
}

export interface GetEquipmentRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：用户名 */
  equipmentId?: string
  /** 查询参数：手机号 */
  equipmentName?: string
}

export interface GetEquipmentData {
  equipmentId: number
  equipmentName: string
  equipmentImg: string
  consumption: number
  attributeExpression: string
  subEquips: string[]
  subEquipsModel: string
  type: number
  equipIds?: EquipmentValue[]
}

export type GetEquipmentResponseData = ApiResponseData<{
  list: GetEquipmentData[]
  total: number
}>

export type GetEquipmentResponseDetail = ApiResponseData<GetEquipmentData>
