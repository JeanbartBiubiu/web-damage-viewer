export class IndivExample{
  Attk!: number;
  Speed!: number;
  HuJia!: number;
  Chuantou!: number;
  ChuantouBaifenbi!: number;
  Baoji!: number;

  constructor(attk:number,speed:number,hujia:number,chuantou:number,chuantoubaifenbi:number,baoji:number){
    this.Attk = attk;
    this.Speed = speed;
    this.HuJia = hujia;
    this.Chuantou = chuantou;
    this.ChuantouBaifenbi = chuantoubaifenbi;
    this.Baoji = baoji;
  }

  Attack(attkObject:IndivExample){
    if (Math.random() >= this.Baoji){
      return this.Attk*(100/((100+((attkObject.HuJia-this.Chuantou)*(1-this.ChuantouBaifenbi))))) * 1.75
    } else{
      return this.Attk*(100/((100+((attkObject.HuJia-this.Chuantou)*(1-this.ChuantouBaifenbi)))))
    }
  }
}

export function AttackTime(timeSpeed:number,objA:IndivExample,ObjB:IndivExample){
  let count = 0,sum = 0
  for(;count <= timeSpeed;count+=1/objA.Speed) {
    sum += objA.Attack(ObjB)

  }
  return sum
}
