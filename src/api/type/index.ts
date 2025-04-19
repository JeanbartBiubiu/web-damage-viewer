import { request } from "@/utils/service"

/** 类型实体 */
export interface Type {
  id?: number
  name: string
  description: string
}

/** 类型传输对象 */
export interface TypeDTO {
  type: Type
  parentTypeIds: number[]
}

/** 类型列表响应 */
export interface TypeResponse {
  list: Type[]
  total: number
}

/**
 * 获取类型列表
 * @param keyword 关键字查询
 */
export function getTypes(keyword?: string): Promise<{ data: Type[] }> {
  return request({
    url: "/types",
    method: "get",
    params: {
      keyword
    }
  })
}

/**
 * 获取类型详情
 * @param id 类型ID
 */
export function getTypeById(id: number): Promise<{ data: Type }> {
  return request({
    url: `/types/${id}`,
    method: "get"
  })
}

/**
 * 创建类型及其关系
 * @param data 类型数据
 */
export function createType(data: TypeDTO): Promise<{ data: boolean }> {
  return request({
    url: "/types",
    method: "post",
    data
  })
}

/**
 * 更新类型及其关系
 * @param id 类型ID
 * @param data 类型数据
 */
export function updateType(id: number, data: TypeDTO): Promise<{ data: boolean }> {
  return request({
    url: `/types/${id}`,
    method: "put",
    data
  })
}

/**
 * 删除类型
 * @param id 类型ID
 */
export function deleteType(id: number): Promise<{ data: boolean }> {
  return request({
    url: `/types/${id}`,
    method: "delete"
  })
}

/**
 * 获取类型的父类型列表
 * @param id 类型ID
 */
export function getParentTypes(id: number): Promise<{ data: Type[] }> {
  return request({
    url: `/types/${id}/parents`,
    method: "get"
  })
}

/**
 * 获取类型的子类型列表
 * @param id 类型ID
 */
export function getChildTypes(id: number): Promise<{ data: Type[] }> {
  return request({
    url: `/types/${id}/children`,
    method: "get"
  })
}
