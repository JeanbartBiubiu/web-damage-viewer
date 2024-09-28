export interface LifeCycleRequestData {
  cycleId?: number
  cycleOrder?: number
  cycleType?: string
  cycleCode?: string
  cycleName?: string
}

export interface GetLifeCycleRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  cycleId?: number
  cycleOrder?: number
  cycleType?: string
  cycleCode?: string
  cycleName?: string
}

export interface GetLifeCycleData {
  cycleId: number
  cycleOrder?: number
  cycleType?: string
  cycleCode?: string
  cycleName?: string
}

export type GetLifeCycleResponseData = ApiResponseData<{
  list: GetLifeCycleData[]
  total: number
}>
