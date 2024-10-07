export interface SkillRequestData {
  skillId?: number
  skillName?: string
  skillImg?: string
  skillType?: number
  skillLevel?: number
  skillConsumption?: string
  skillCd?: string
  skillCheckAndDo?: string
  skillCastPoint?: string
  skillCastPointAfter?: string
}

export interface GetSkillRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：用户名 */
  skillId?: string
  /** 查询参数：手机号 */
  skillName?: string
}

export interface GetSkillData {
  skillId: number
  skillName: string
  skillImg: string
  skillType: number
  skillLevel: number
  skillConsumption: string
  skillCd: string
  skillCheckAndDo: string
  skillCastPoint: string
  skillCastPointAfter: string
}

export type GetSkillResponseData = ApiResponseData<{
  list: GetSkillData[]
  total: number
}>
