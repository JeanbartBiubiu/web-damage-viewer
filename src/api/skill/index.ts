import { request } from "@/utils/service"
import type * as Skill from "./types/skill"


/** 增 */
export function createSkill(data: Skill.SkillRequestData) {
  return request({
    url: "skill",
    method: "post",
    data
  })
}

/** 删 */
export function deleteSkill(id: number) {
  return request({
    url: `skill/${id}`,
    method: "delete"
  })
}

/** 改 */
export function updateSkill(data: Skill.SkillRequestData) {
  return request({
    url: "skill",
    method: "put",
    data
  })
}

/** 查 */
export function getSkill(params: Skill.SkillRequestData) {
  return request<Skill.GetSkillResponseData>({
    url: "skill",
    method: "get",
    params
  })
}

