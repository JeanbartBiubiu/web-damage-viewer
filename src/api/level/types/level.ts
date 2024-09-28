export interface LevelRequestData {
  levelId?: number
  levelName?: string
  levelImg?: string
}

export interface GetLevelRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：用户名 */
  levelId?: string
  /** 查询参数：手机号 */
  levelName?: string
}

export interface GetLevelData {
  levelId: number
  levelName: string
  levelImg: string
}

export type GetLevelResponseData = ApiResponseData<{
  list: GetLevelData[]
  total: number
}>
