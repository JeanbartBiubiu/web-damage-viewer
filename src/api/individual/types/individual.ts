export interface IndivRequestData {
  indivId?: number
  indivName?: string
  indivImg?: string
}

export interface IndivAttributeValueRequestData {
  indivId?: number
  levelId?: number
  attributeId?: number
  value?:number
}

export interface GetIndivRequestData {
  /** 当前页码 */
  currentPage: number
  /** 查询条数 */
  size: number
  /** 查询参数：用户名 */
  indivId?: string
  /** 查询参数：手机号 */
  indivName?: string
}

export interface GetIndivData {
  indivId: number
  indivName: string
  indivImg: string
}

export interface GetIndivAttributeValueData {
  indivId: number
  levelId: number
  attributeId: number
  value:number
}

export interface IndivAttributeValueHolisticData {
  attributeId?: number
  attributeName?: string
  value1?: number
  value2?: number
  value3?: number
  value4?: number
  value5?: number
  value6?: number
  value7?: number
  value8?: number
  value9?: number
  value10?: number
  value11?: number
  value12?: number
  value13?: number
  value14?: number
  value15?: number
  value16?: number
  value17?: number
  value18?: number
  value19?: number
  value20?: number
  value21?: number
  value22?: number
  value23?: number
  value24?: number
  value25?: number
  value26?: number
  value27?: number
  value28?: number
  value29?: number
  value30?: number
  value31?: number
  value32?: number
  value33?: number
  value34?: number
  value35?: number
  value36?: number
  value37?: number
  value38?: number
  value39?: number
  value40?: number
  value41?: number
  value42?: number
  value43?: number
  value44?: number
  value45?: number
  value46?: number
  value47?: number
  value48?: number
  value49?: number
  value50?: number
}

export function settingHolisticValue(index: number,value: number,item: IndivAttributeValueHolisticData) {
  switch (index){
    case 1: item.value1 = value;break;
    case 2: item.value2 = value;break;
    case 3: item.value3 = value;break;
    case 4: item.value4 = value;break;
    case 5: item.value5 = value;break;
    case 6: item.value6 = value;break;
    case 7: item.value7 = value;break;
    case 8: item.value8 = value;break;
    case 9: item.value9 = value;break;
    case 10: item.value10 = value;break;
    case 11: item.value11 = value;break;
    case 12: item.value12 = value;break;
    case 13: item.value13 = value;break;
    case 14: item.value14 = value;break;
    case 15: item.value15 = value;break;
    case 16: item.value16 = value;break;
    case 17: item.value17 = value;break;
    case 18: item.value18 = value;break;
    case 19: item.value19 = value;break;
    case 20: item.value20 = value;break;
    case 21: item.value21 = value;break;
    case 22: item.value22 = value;break;
    case 23: item.value23 = value;break;
    case 24: item.value24 = value;break;
    case 25: item.value25 = value;break;
    case 26: item.value26 = value;break;
    case 27: item.value27 = value;break;
    case 28: item.value28 = value;break;
    case 29: item.value29 = value;break;
    case 30: item.value30 = value;break;
    case 31: item.value31 = value;break;
    case 32: item.value32 = value;break;
    case 33: item.value33 = value;break;
    case 34: item.value34 = value;break;
    case 35: item.value35 = value;break;
    case 36: item.value36 = value;break;
    case 37: item.value37 = value;break;
    case 38: item.value38 = value;break;
    case 39: item.value39 = value;break;
    case 40: item.value40 = value;break;
    case 41: item.value41 = value;break;
    case 42: item.value42 = value;break;
    case 43: item.value43 = value;break;
    case 44: item.value44 = value;break;
    case 45: item.value45 = value;break;
    case 46: item.value46 = value;break;
    case 47: item.value47 = value;break;
    case 48: item.value48 = value;break;
    case 49: item.value49 = value;break;
    case 50: item.value50 = value;break;
  }

}

export type GetIndivResponseData = ApiResponseData<{
  list: GetIndivData[]
  total: number
}>

export type GetIndivValueResponseData = ApiResponseData<{
  list: GetIndivAttributeValueData[]
  total: number
}>

